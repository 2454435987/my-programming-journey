package com.water.delivery.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TicketRechargeDTO {
    @NotNull(message = "客户ID不能为空")
    private Long customerId;
    @NotNull(message = "水票规格ID不能为空")
    private Long typeId;
    @NotNull(message = "购买张数不能为空")
    @Min(value = 1, message = "购买张数至少为1张")
    private Integer buyQty;
    private Integer giftQty = 0; // 赠送张数
    private BigDecimal payAmount; // 实收金额
    private String remark;
}
