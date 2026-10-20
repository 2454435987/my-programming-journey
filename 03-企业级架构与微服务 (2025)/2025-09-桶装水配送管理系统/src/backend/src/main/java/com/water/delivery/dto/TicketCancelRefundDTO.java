package com.water.delivery.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TicketCancelRefundDTO {
    @NotNull(message = "客户ID不能为空")
    private Long customerId;
    @NotNull(message = "水票规格ID不能为空")
    private Long typeId;
    @NotNull(message = "退作废张数不能为空")
    @Min(value = 1, message = "至少扣减1张")
    private Integer qty;
    private BigDecimal refundAmount; // 退款金额
    @NotBlank(message = "作废/退票原因必须详细填写")
    private String reason;
    private Integer bizType = 3; // 3=作废, 4=退票回收
}
