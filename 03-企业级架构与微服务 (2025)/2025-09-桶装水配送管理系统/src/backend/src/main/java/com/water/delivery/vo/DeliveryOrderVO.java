package com.water.delivery.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DeliveryOrderVO {
    private Long id;
    private String orderNo;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String addressDetail;
    private Integer floorNum;
    private Integer hasElevator;
    private Long courierId;
    private String courierName;
    private Long typeId;
    private String brandName;
    private String specDesc;
    private Integer deliveryQty;
    private Integer recycleQty;
    private Integer barrelDiff;
    private String aiInspectStatus;
    private String aiInspectDesc;
    private BigDecimal suggestDeductDeposit;
    private String inspectImageUrl;
    private Date deliveryTime;
    private Integer orderStatus;
    private Integer costMinutes;
    private Integer isOverdue;
    private String signMethod;
}
