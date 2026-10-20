package com.water.delivery.vo;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DashboardMetricsVO {
    private Integer warningCustomerCount; // 低余票告急客户总数
    private Integer totalDeliveredToday;   // 今日累计送水桶数
    private Integer totalRecycledToday;    // 今日累计回桶只数
    private Double recycleRate;            // 空桶回收率 (%)
    private Double onTimeRate;             // 履约准时率 (%)
    private Long onTimeCount;              // 准时单量
    private Long overdueCount;             // 逾期单量
    private List<CustomerVO> warningCustomers; // 低余票预警 TOP10
    private List<Map<String, Object>> courierWorkloadRank; // 送水工工作量排行榜
    private List<Map<String, Object>> courierRankings;     // 送水工工作量排行榜别名
    private Map<String, Object> timelinessDistribution; // 按期 vs 逾期分布数据
}
