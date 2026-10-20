package com.water.delivery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_delivery_order")
public class DeliveryOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long customerId;
    private Long courierId;
    private Long typeId;
    private Integer deliveryQty;
    private Integer recycleQty;
    private Date deliveryTime;
    private Integer orderStatus; // 1=已核销完成, 2=异常争议
    private Integer costMinutes;
    private Integer isOverdue; // 1=超时逾期, 0=按期送达
    private String signMethod;
}
