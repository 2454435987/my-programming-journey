package com.water.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourierDTO {
    private Long id;
    private String courierNo;
    @NotBlank(message = "送水工姓名不能为空")
    private String name;
    @NotBlank(message = "手机号码不能为空")
    private String phone;
    @NotNull(message = "负责片区不能为空")
    private Long regionId;
    private String vehicleType;
    private Integer status; // 1=在职, 2=休假, 0=停用
    private String idCard;
}
