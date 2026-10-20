package com.water.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.water.delivery.common.exception.BusinessException;
import com.water.delivery.dto.CustomerDTO;
import com.water.delivery.dto.CustomerQueryDTO;
import com.water.delivery.entity.*;
import com.water.delivery.mapper.*;
import com.water.delivery.service.CustomerService;
import com.water.delivery.vo.CustomerVO;
import com.water.delivery.vo.TicketAccountVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    private final RegionMapper regionMapper;
    private final TicketAccountMapper ticketAccountMapper;
    private final TicketTypeMapper ticketTypeMapper;

    @Override
    public Page<CustomerVO> queryPage(CustomerQueryDTO queryDTO) {
        Page<Customer> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(queryDTO.getKeyword())) {
            wrapper.and(w -> w.like(Customer::getName, queryDTO.getKeyword())
                    .or().like(Customer::getPhone, queryDTO.getKeyword())
                    .or().like(Customer::getCustomerNo, queryDTO.getKeyword())
                    .or().like(Customer::getAddressDetail, queryDTO.getKeyword()));
        }
        if (queryDTO.getRegionId() != null) {
            wrapper.eq(Customer::getRegionId, queryDTO.getRegionId());
        }
        wrapper.orderByDesc(Customer::getId);

        Page<Customer> selectPage = baseMapper.selectPage(page, wrapper);

        // 缓存片区名称和水票类型
        Map<Long, String> regionMap = regionMapper.selectList(null).stream()
                .collect(Collectors.toMap(Region::getId, Region::getRegionName, (k1, k2) -> k1));
        Map<Long, TicketType> typeMap = ticketTypeMapper.selectList(null).stream()
                .collect(Collectors.toMap(TicketType::getId, t -> t, (k1, k2) -> k1));

        List<CustomerVO> voList = new ArrayList<>();
        for (Customer c : selectPage.getRecords()) {
            CustomerVO vo = convertToVO(c, regionMap, typeMap);
            if (Boolean.TRUE.equals(queryDTO.getOnlyWarning()) && !vo.getIsWarning()) {
                continue;
            }
            voList.add(vo);
        }

        Page<CustomerVO> resultPage = new Page<>(queryDTO.getPage(), queryDTO.getSize(), selectPage.getTotal());
        resultPage.setRecords(voList);
        return resultPage;
    }

    @Override
    public CustomerVO getCustomerDetail(Long id) {
        Customer customer = baseMapper.selectById(id);
        if (customer == null) throw new BusinessException("客户不存在");
        Map<Long, String> regionMap = regionMapper.selectList(null).stream()
                .collect(Collectors.toMap(Region::getId, Region::getRegionName, (k1, k2) -> k1));
        Map<Long, TicketType> typeMap = ticketTypeMapper.selectList(null).stream()
                .collect(Collectors.toMap(TicketType::getId, t -> t, (k1, k2) -> k1));
        return convertToVO(customer, regionMap, typeMap);
    }

    @Override
    public List<TicketAccountVO> getCustomerTickets(Long customerId) {
        List<TicketAccount> accounts = ticketAccountMapper.selectList(new LambdaQueryWrapper<TicketAccount>()
                .eq(TicketAccount::getCustomerId, customerId));
        Map<Long, TicketType> typeMap = ticketTypeMapper.selectList(null).stream()
                .collect(Collectors.toMap(TicketType::getId, t -> t, (k1, k2) -> k1));

        List<TicketAccountVO> list = new ArrayList<>();
        for (TicketAccount ta : accounts) {
            TicketAccountVO vo = new TicketAccountVO();
            vo.setId(ta.getId());
            vo.setCustomerId(ta.getCustomerId());
            vo.setTypeId(ta.getTypeId());
            vo.setBalance(ta.getBalance());
            vo.setTotalRecharge(ta.getTotalRecharge());
            vo.setTotalConsumed(ta.getTotalConsumed());

            TicketType tt = typeMap.get(ta.getTypeId());
            if (tt != null) {
                vo.setBrandName(tt.getBrandName());
                vo.setSpecDesc(tt.getSpecDesc());
                vo.setUnitPrice(tt.getUnitPrice());
            }
            list.add(vo);
        }
        return list;
    }

    @Override
    public List<CustomerVO> searchCustomer(String keyword) {
        if (!StringUtils.hasText(keyword)) return Collections.emptyList();
        List<Customer> list = baseMapper.selectList(new LambdaQueryWrapper<Customer>()
                .like(Customer::getPhone, keyword)
                .or().like(Customer::getName, keyword)
                .last("LIMIT 10"));

        Map<Long, String> regionMap = regionMapper.selectList(null).stream()
                .collect(Collectors.toMap(Region::getId, Region::getRegionName, (k1, k2) -> k1));
        Map<Long, TicketType> typeMap = ticketTypeMapper.selectList(null).stream()
                .collect(Collectors.toMap(TicketType::getId, t -> t, (k1, k2) -> k1));

        return list.stream().map(c -> convertToVO(c, regionMap, typeMap)).collect(Collectors.toList());
    }

    @Override
    public boolean saveOrUpdateCustomer(CustomerDTO dto) {
        if (dto.getId() == null) {
            String customerNo = dto.getCustomerNo();
            if (!StringUtils.hasText(customerNo)) {
                customerNo = "KH" + (int)((Math.random() * 9 + 1) * 100000);
            }
            Customer customer = new Customer();
            customer.setCustomerNo(customerNo);
            customer.setName(dto.getName());
            customer.setPhone(dto.getPhone());
            customer.setRegionId(dto.getRegionId());
            customer.setAddressDetail(dto.getAddressDetail());
            customer.setFloorNum(dto.getFloorNum() != null ? dto.getFloorNum() : 1);
            customer.setHasElevator(dto.getHasElevator() != null ? dto.getHasElevator() : 1);
            customer.setCustomerType(dto.getCustomerType() != null ? dto.getCustomerType() : 1);
            customer.setUnreturnedBarrels(0);
            customer.setWarningThreshold(dto.getWarningThreshold() != null ? dto.getWarningThreshold() : 2);
            customer.setCreateTime(new Date());
            baseMapper.insert(customer);
        } else {
            Customer customer = baseMapper.selectById(dto.getId());
            if (customer == null) throw new BusinessException("客户不存在");
            customer.setName(dto.getName());
            customer.setPhone(dto.getPhone());
            customer.setRegionId(dto.getRegionId());
            customer.setAddressDetail(dto.getAddressDetail());
            if (dto.getFloorNum() != null) customer.setFloorNum(dto.getFloorNum());
            if (dto.getHasElevator() != null) customer.setHasElevator(dto.getHasElevator());
            if (dto.getCustomerType() != null) customer.setCustomerType(dto.getCustomerType());
            if (dto.getWarningThreshold() != null) customer.setWarningThreshold(dto.getWarningThreshold());
            baseMapper.updateById(customer);
        }
        return true;
    }

    private CustomerVO convertToVO(Customer c, Map<Long, String> regionMap, Map<Long, TicketType> typeMap) {
        CustomerVO vo = new CustomerVO();
        vo.setId(c.getId());
        vo.setCustomerNo(c.getCustomerNo());
        vo.setName(c.getName());
        vo.setPhone(c.getPhone());
        vo.setRegionId(c.getRegionId());
        vo.setRegionName(regionMap.getOrDefault(c.getRegionId(), "未知片区"));
        vo.setAddressDetail(c.getAddressDetail());
        vo.setFloorNum(c.getFloorNum());
        vo.setHasElevator(c.getHasElevator());
        vo.setCustomerType(c.getCustomerType());
        vo.setUnreturnedBarrels(c.getUnreturnedBarrels());
        vo.setWarningThreshold(c.getWarningThreshold());
        vo.setCreateTime(c.getCreateTime());

        // 查询各品牌水票账户明细
        List<TicketAccount> accounts = ticketAccountMapper.selectList(new LambdaQueryWrapper<TicketAccount>()
                .eq(TicketAccount::getCustomerId, c.getId()));
        int totalBal = 0;
        List<TicketAccountVO> accVOs = new ArrayList<>();
        for (TicketAccount ta : accounts) {
            totalBal += ta.getBalance();
            TicketAccountVO avo = new TicketAccountVO();
            avo.setId(ta.getId());
            avo.setCustomerId(ta.getCustomerId());
            avo.setTypeId(ta.getTypeId());
            avo.setBalance(ta.getBalance());
            avo.setTotalRecharge(ta.getTotalRecharge());
            avo.setTotalConsumed(ta.getTotalConsumed());
            TicketType tt = typeMap.get(ta.getTypeId());
            if (tt != null) {
                avo.setBrandName(tt.getBrandName());
                avo.setSpecDesc(tt.getSpecDesc());
                avo.setUnitPrice(tt.getUnitPrice());
            }
            accVOs.add(avo);
        }
        vo.setTotalBalance(totalBal);
        vo.setIsWarning(totalBal <= c.getWarningThreshold());
        vo.setAccounts(accVOs);
        return vo;
    }
}
