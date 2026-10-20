package com.baidu.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("location")
@NoArgsConstructor
@AllArgsConstructor
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 地址ID
     */
    @TableId(value = "aid", type = IdType.AUTO)
    private Integer aid;

    /**
     * 收货人姓名
     */
    @TableField("realName")
    private String realName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 身份证号
     */
    private String identity;


    /**
     * 收货地址
     */
    private String address;

    /**
     * 订单ID
     */
    private Integer oid;


    /**
     * 用户ID
     */
    private Integer uid;


    @TableField(exist = false)
    private Order order;

    @TableField(exist = false)
    private User user;

}
