package com.water.delivery.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiInspectVO {
    private String status; // PASSED, NEED_CLEAN, SCRAP
    private String statusLabel; // 合格品 / 需深度清洗 / 严重破损建议报废
    private String description; // AI诊断损伤与卫生分析说明
    private BigDecimal suggestDeductDeposit; // 建议扣除押金
    private String inspectImageUrl; // 存证图片URL
}
