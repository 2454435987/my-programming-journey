package com.water.delivery.dto;

import lombok.Data;

@Data
public class DeliveryHistoryQueryDTO {
    private Integer page = 1;
    private Integer size = 10;
    private Long customerId;
    private Long courierId;
    private Long regionId;
    private String startTime; // yyyy-MM-dd HH:mm:ss
    private String endTime;
    private String keyword;
    private Integer orderStatus;
}
