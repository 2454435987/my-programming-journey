package com.water.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.water.delivery.common.exception.BusinessException;
import com.water.delivery.dto.TicketCancelRefundDTO;
import com.water.delivery.dto.TicketRechargeDTO;
import com.water.delivery.entity.*;
import com.water.delivery.mapper.*;
import com.water.delivery.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketTypeMapper ticketTypeMapper;
    private final TicketAccountMapper ticketAccountMapper;
    private final TicketFlowMapper ticketFlowMapper;
    private final CustomerMapper customerMapper;

    @Override
    public List<TicketType> listActiveTypes() {
        return ticketTypeMapper.selectList(new LambdaQueryWrapper<TicketType>()
                .eq(TicketType::getIsActive, 1));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean recharge(TicketRechargeDTO dto, Long operatorId) {
        Customer customer = customerMapper.selectById(dto.getCustomerId());
        if (customer == null) throw new BusinessException("客户不存在");

        TicketType tt = ticketTypeMapper.selectById(dto.getTypeId());
        if (tt == null) throw new BusinessException("水票规格不存在");

        int addQty = dto.getBuyQty() + (dto.getGiftQty() != null ? dto.getGiftQty() : 0);

        // 1. 查询水票账户
        TicketAccount account = ticketAccountMapper.selectOne(new LambdaQueryWrapper<TicketAccount>()
                .eq(TicketAccount::getCustomerId, dto.getCustomerId())
                .eq(TicketAccount::getTypeId, dto.getTypeId()));

        int beforeBal = (account != null) ? account.getBalance() : 0;
        int afterBal = beforeBal + addQty;

        if (account == null) {
            account = new TicketAccount();
            account.setCustomerId(dto.getCustomerId());
            account.setTypeId(dto.getTypeId());
            account.setBalance(afterBal);
            account.setTotalRecharge(addQty);
            account.setTotalConsumed(0);
            account.setVersion(0);
            ticketAccountMapper.insert(account);
        } else {
            account.setBalance(afterBal);
            account.setTotalRecharge(account.getTotalRecharge() + addQty);
            ticketAccountMapper.updateById(account);
        }

        // 2. 写入充值发放流水 (bizType=1)
        TicketFlow flow = new TicketFlow();
        flow.setFlowNo("TF" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + (int)((Math.random()*9+1)*100));
        flow.setCustomerId(dto.getCustomerId());
        flow.setTypeId(dto.getTypeId());
        flow.setBizType(1);
        flow.setChangeAmount(addQty);
        flow.setBeforeBalance(beforeBal);
        flow.setAfterBalance(afterBal);
        flow.setOperatorId(operatorId != null ? operatorId : 1L);
        flow.setRemark(dto.getRemark() != null ? dto.getRemark() : ("充值购买 " + dto.getBuyQty() + " 张，赠送 " + dto.getGiftQty() + " 张"));
        flow.setCreateTime(new Date());
        ticketFlowMapper.insert(flow);

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean cancelOrRefund(TicketCancelRefundDTO dto, Long operatorId) {
        TicketAccount account = ticketAccountMapper.selectOne(new LambdaQueryWrapper<TicketAccount>()
                .eq(TicketAccount::getCustomerId, dto.getCustomerId())
                .eq(TicketAccount::getTypeId, dto.getTypeId()));

        if (account == null || account.getBalance() < dto.getQty()) {
            throw new BusinessException("操作失败：客户该水票可用余票不足，无法扣减");
        }

        int beforeBal = account.getBalance();
        int afterBal = beforeBal - dto.getQty();

        account.setBalance(afterBal);
        ticketAccountMapper.updateById(account);

        // 写入作废/退款流水 (bizType=3作废, 4退票)
        TicketFlow flow = new TicketFlow();
        flow.setFlowNo("TF" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + (int)((Math.random()*9+1)*100));
        flow.setCustomerId(dto.getCustomerId());
        flow.setTypeId(dto.getTypeId());
        flow.setBizType(dto.getBizType());
        flow.setChangeAmount(-dto.getQty());
        flow.setBeforeBalance(beforeBal);
        flow.setAfterBalance(afterBal);
        flow.setOperatorId(operatorId != null ? operatorId : 1L);
        flow.setRemark(dto.getReason());
        flow.setCreateTime(new Date());
        ticketFlowMapper.insert(flow);

        return true;
    }

    @Override
    public Page<Map<String, Object>> queryFlowPage(int pageNum, int pageSize, Long customerId, String keyword, Integer bizType, String startDate, String endDate) {
        Page<TicketFlow> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<TicketFlow> wrapper = new LambdaQueryWrapper<>();
        if (customerId != null) {
            wrapper.eq(TicketFlow::getCustomerId, customerId);
        }
        if (bizType != null) {
            wrapper.eq(TicketFlow::getBizType, bizType);
        }
        if (StringUtils.hasText(startDate)) {
            wrapper.apply("DATE(create_time) >= {0}", startDate.trim());
        }
        if (StringUtils.hasText(endDate)) {
            wrapper.apply("DATE(create_time) <= {0}", endDate.trim());
        }
        if (StringUtils.hasText(keyword)) {
            String kw = keyword.trim();
            List<Long> matchedCustomerIds = customerMapper.selectList(new LambdaQueryWrapper<Customer>()
                    .like(Customer::getName, kw)
                    .or()
                    .like(Customer::getPhone, kw))
                    .stream().map(Customer::getId).collect(Collectors.toList());

            wrapper.and(w -> {
                w.like(TicketFlow::getFlowNo, kw)
                 .or()
                 .like(TicketFlow::getRemark, kw);
                if (!matchedCustomerIds.isEmpty()) {
                    w.or().in(TicketFlow::getCustomerId, matchedCustomerIds);
                }
            });
        }
        wrapper.orderByDesc(TicketFlow::getId);

        Page<TicketFlow> selectPage = ticketFlowMapper.selectPage(page, wrapper);

        Map<Long, Customer> custMap = new HashMap<>();
        Map<Long, TicketType> typeMap = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> records = new ArrayList<>();
        for (TicketFlow tf : selectPage.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", tf.getId());
            map.put("flowNo", tf.getFlowNo());
            map.put("customerId", tf.getCustomerId());
            map.put("typeId", tf.getTypeId());
            map.put("bizType", tf.getBizType());
            map.put("changeAmount", tf.getChangeAmount());
            map.put("beforeBalance", tf.getBeforeBalance());
            map.put("afterBalance", tf.getAfterBalance());
            map.put("refOrderNo", tf.getRefOrderNo());
            map.put("operatorId", tf.getOperatorId());
            map.put("remark", tf.getRemark());
            map.put("createTime", tf.getCreateTime() != null ? sdf.format(tf.getCreateTime()) : "");

            Customer c = custMap.computeIfAbsent(tf.getCustomerId(), cid -> customerMapper.selectById(cid));
            TicketType tt = typeMap.computeIfAbsent(tf.getTypeId(), tid -> ticketTypeMapper.selectById(tid));

            map.put("customerName", c != null ? c.getName() : "未知客户");
            map.put("customerPhone", c != null ? c.getPhone() : "");
            map.put("brandSpecName", tt != null ? tt.getBrandName() + "(" + tt.getSpecDesc() + ")" : "未知规格");
            records.add(map);
        }

        Page<Map<String, Object>> resultPage = new Page<>(pageNum, pageSize, selectPage.getTotal());
        resultPage.setRecords(records);
        return resultPage;
    }
}
