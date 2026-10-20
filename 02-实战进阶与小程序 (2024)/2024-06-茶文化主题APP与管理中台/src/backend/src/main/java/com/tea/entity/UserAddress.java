package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 收货地址实体
 * 表名：user_address
 */
@Data
@TableName("user_address")
public class UserAddress {

    /** 地址ID */
    @TableId(value = "address_id", type = IdType.AUTO)
    private Long addressId;
    /** 用户ID */
    @TableField("user_id")
    private Long userId;
    /** 收货人 */
    @TableField("receiver")
    private String receiver;
    /** 手机号 */
    @TableField("phone")
    private String phone;
    /** 省 */
    @TableField("province")
    private String province;
    /** 市 */
    @TableField("city")
    private String city;
    /** 区 */
    @TableField("district")
    private String district;
    /** 详细地址 */
    @TableField("detail")
    private String detail;
    /** 是否默认 */
    @TableField("is_default")
    private Integer isDefault;
    /** 创建时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
