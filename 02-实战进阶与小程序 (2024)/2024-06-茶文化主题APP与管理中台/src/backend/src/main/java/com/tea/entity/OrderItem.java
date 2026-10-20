package com.tea.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 订单明细实体
 * 表名：order_item
 */
@Data
@TableName("order_item")
public class OrderItem {

    /** 明细ID */
    @TableId(value = "item_id", type = IdType.AUTO)
    private Long itemId;
    /** 订单ID */
    @TableField("order_id")
    private Long orderId;
    /** 订单编号 */
    @TableField("order_no")
    private String orderNo;
    /** 商品ID */
    @TableField("product_id")
    private Long productId;
    /** 商品名称 */
    @TableField("product_name")
    private String productName;
    /** 商品图 */
    @TableField("product_image")
    private String productImage;
    /** 成交单价 */
    @TableField("price")
    private BigDecimal price;
    /** 数量 */
    @TableField("quantity")
    private Integer quantity;
}
