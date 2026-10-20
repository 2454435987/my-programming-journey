package com.water.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.water.delivery.dto.CustomerQueryDTO;
import com.water.delivery.entity.*;
import com.water.delivery.mapper.*;
import com.water.delivery.service.CustomerService;
import com.water.delivery.service.DashboardService;
import com.water.delivery.vo.CustomerVO;
import com.water.delivery.vo.DashboardMetricsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CustomerService customerService;
    private final DeliveryOrderMapper deliveryOrderMapper;
    private final CourierMapper courierMapper;
    private final BarrelRecycleMapper barrelRecycleMapper;

    @Override
    public DashboardMetricsVO getMetrics() {
        DashboardMetricsVO vo = new DashboardMetricsVO();

        // 1. 低余票告急预警列表 (取前10名)
        CustomerQueryDTO query = new CustomerQueryDTO();
        query.setPage(1);
        query.setSize(50);
        query.setOnlyWarning(true);
        List<CustomerVO> warningCusts = customerService.queryPage(query).getRecords();

        // 按余票升序排列
        warningCusts.sort(Comparator.comparingInt(CustomerVO::getTotalBalance));
        vo.setWarningCustomerCount(warningCusts.size());
        vo.setWarningCustomers(warningCusts.size() > 10 ? warningCusts.subList(0, 10) : warningCusts);

        // 2. 全站送水与回桶总量汇总
        List<DeliveryOrder> allOrders = deliveryOrderMapper.selectList(null);
        int totalDelivered = allOrders.stream().mapToInt(DeliveryOrder::getDeliveryQty).sum();
        int totalRecycled = allOrders.stream().mapToInt(DeliveryOrder::getRecycleQty).sum();
        vo.setTotalDeliveredToday(totalDelivered);
        vo.setTotalRecycledToday(totalRecycled);

        // 3. 履约时效统计 (按期送达 vs 超时逾期)
        long onTimeCount = allOrders.stream().filter(o -> o.getIsOverdue() == 0).count();
        long overdueCount = allOrders.size() - onTimeCount;
        double onTimeRate = (allOrders.size() > 0) ?
                BigDecimal.valueOf((double) onTimeCount / allOrders.size() * 100).setScale(1, RoundingMode.HALF_UP).doubleValue() : 100.0;
        vo.setOnTimeRate(onTimeRate);
        vo.setOnTimeCount(onTimeCount);
        vo.setOverdueCount(overdueCount);
        double overallRecycleRate = (totalDelivered > 0) ?
                BigDecimal.valueOf((double) totalRecycled / totalDelivered * 100).setScale(1, RoundingMode.HALF_UP).doubleValue() : 0.0;
        vo.setRecycleRate(overallRecycleRate);

        Map<String, Object> timeliness = new HashMap<>();
        timeliness.put("onTimeCount", onTimeCount);
        timeliness.put("overdueCount", overdueCount);
        timeliness.put("onTimeRate", onTimeRate);
        vo.setTimelinessDistribution(timeliness);

        // 4. 送水工工作量与回桶效率排行榜
        List<Courier> couriers = courierMapper.selectList(new LambdaQueryWrapper<Courier>().eq(Courier::getStatus, 1));
        List<Map<String, Object>> rankList = new ArrayList<>();

        for (Courier cr : couriers) {
            List<DeliveryOrder> orders = deliveryOrderMapper.selectList(new LambdaQueryWrapper<DeliveryOrder>()
                    .eq(DeliveryOrder::getCourierId, cr.getId()));
            int delSum = orders.stream().mapToInt(DeliveryOrder::getDeliveryQty).sum();
            int recSum = orders.stream().mapToInt(DeliveryOrder::getRecycleQty).sum();
            double recRate = (delSum > 0) ?
                    BigDecimal.valueOf((double) recSum / delSum * 100).setScale(1, RoundingMode.HALF_UP).doubleValue() : 0.0;

            Map<String, Object> row = new HashMap<>();
            row.put("courierId", cr.getId());
            row.put("courierNo", cr.getCourierNo());
            row.put("courierName", cr.getName());
            row.put("name", cr.getName());
            row.put("phone", cr.getPhone());
            row.put("deliveryQty", delSum);
            row.put("recycleQty", recSum);
            row.put("totalDelivered", delSum);
            row.put("totalRecycled", recSum);
            row.put("recycleRate", recRate);
            rankList.add(row);
        }

        // 按送水量降序
        rankList.sort((a, b) -> Integer.compare((Integer) b.get("deliveryQty"), (Integer) a.get("deliveryQty")));
        vo.setCourierWorkloadRank(rankList);
        vo.setCourierRankings(rankList);

        return vo;
    }
}
