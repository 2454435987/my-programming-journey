package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 购物车实体
 * 表名：cart_item
 */
@Data
@TableName("cart_item")
public class CartItem {

    /** 购物车ID */
    @TableId(value = "cart_id", type = IdType.AUTO)
    private Long cartId;
    /** 用户ID */
    @TableField("user_id")
    private Long userId;
    /** 商品ID */
    @TableField("product_id")
    private Long productId;
    /** 数量 */
    @TableField("quantity")
    private Integer quantity;
    /** 是否选中 */
    @TableField("checked")
    private Integer checked;
    /** 加入时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
