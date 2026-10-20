package com.tea.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 订单实体
 * 表名：orders
 */
@Data
@TableName("orders")
public class Orders {

    /** 订单ID */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;
    /** 订单编号 */
    @TableField("order_no")
    private String orderNo;
    /** 用户ID */
    @TableField("user_id")
    private Long userId;
    /** 订单总额 */
    @TableField("total_amount")
    private BigDecimal totalAmount;
    /** 实付金额 */
    @TableField("pay_amount")
    private BigDecimal payAmount;
    /** 运费 */
    @TableField("freight")
    private BigDecimal freight;
    /** 状态 */
    @TableField("status")
    private Integer status;
    /** 支付方式 */
    @TableField("pay_type")
    private Integer payType;
    /** 支付时间 */
    @TableField("pay_time")
    private LocalDateTime payTime;
    /** 发货时间 */
    @TableField("ship_time")
    private LocalDateTime shipTime;
    /** 收货时间 */
    @TableField("receive_time")
    private LocalDateTime receiveTime;
    /** 收货人 */
    @TableField("receiver")
    private String receiver;
    /** 收货电话 */
    @TableField("receiver_phone")
    private String receiverPhone;
    /** 收货地址 */
    @TableField("receiver_address")
    private String receiverAddress;
    /** 买家备注 */
    @TableField("remark")
    private String remark;
    /** 下单时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
    /** 更新时间 */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
