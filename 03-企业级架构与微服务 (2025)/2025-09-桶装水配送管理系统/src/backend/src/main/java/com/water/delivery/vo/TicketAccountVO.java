package com.water.delivery.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TicketAccountVO {
    private Long id;
    private Long customerId;
    private Long typeId;
    private String brandName;
    private String specDesc;
    private BigDecimal unitPrice;
    private Integer balance;
    private Integer totalRecharge;
    private Integer totalConsumed;
}
