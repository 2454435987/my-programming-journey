package com.water.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;
    private String customerNo;
    @NotBlank(message = "客户姓名不能为空")
    private String name;
    @NotBlank(message = "联系电话不能为空")
    private String phone;
    @NotNull(message = "所属片区不能为空")
    private Long regionId;
    @NotBlank(message = "详细门牌地址不能为空")
    private String addressDetail;
    private Integer floorNum = 1;
    private Integer hasElevator = 1; // 1=有电梯, 0=无电梯步梯
    private Integer customerType = 1; // 1=家庭散客, 2=企业大客户
    private Integer warningThreshold = 2; // 预警阈值
}
