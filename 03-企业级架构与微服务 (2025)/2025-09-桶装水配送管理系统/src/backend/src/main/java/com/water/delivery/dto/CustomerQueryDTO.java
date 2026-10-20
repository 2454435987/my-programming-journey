package com.water.delivery.dto;

import lombok.Data;

@Data
public class CustomerQueryDTO {
    private Integer page = 1;
    private Integer size = 10;
    private String keyword; // 姓名或手机号模糊检索
    private Long regionId;  // 片区过滤
    private Boolean onlyWarning; // 是否仅查看余票告急客户
}
