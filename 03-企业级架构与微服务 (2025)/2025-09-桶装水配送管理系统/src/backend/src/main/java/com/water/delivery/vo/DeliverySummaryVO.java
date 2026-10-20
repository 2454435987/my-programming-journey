package com.water.delivery.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliverySummaryVO {
    private Integer totalDelivered; // 累计配送量 (桶)
    private Integer totalRecycled;  // 累计回收量 (只)
    private Double recycleRate;     // 综合回桶率 (%)
    private Integer netDiff;        // 空桶欠还差额
}
