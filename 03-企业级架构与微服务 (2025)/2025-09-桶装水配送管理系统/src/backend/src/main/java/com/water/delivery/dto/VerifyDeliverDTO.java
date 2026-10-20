package com.water.delivery.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class VerifyDeliverDTO {
    @NotNull(message = "客户ID不能为空")
    private Long customerId;
    @NotNull(message = "送水工ID不能为空")
    private Long courierId;
    @NotNull(message = "水票规格ID不能为空")
    private Long typeId;
    @NotNull(message = "配送桶数不能为空")
    @Min(value = 1, message = "配送数量至少为1桶")
    private Integer deliveryQty = 1;
    private Integer recycleQty = 0; // 实收空桶数
    private String aiInspectStatus = "PASSED"; // PASSED, NEED_CLEAN, SCRAP
    private String aiInspectDesc;
    private BigDecimal suggestDeductDeposit = BigDecimal.ZERO;
    private String inspectImageUrl;
    private String signMethod = "核销码确认";
}
