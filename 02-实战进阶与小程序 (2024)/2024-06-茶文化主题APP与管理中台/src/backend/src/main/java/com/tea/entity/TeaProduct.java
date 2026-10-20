package com.tea.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 茶品商品实体
 * 表名：tea_product
 */
@Data
@TableName("tea_product")
public class TeaProduct {

    /** 商品ID */
    @TableId(value = "product_id", type = IdType.AUTO)
    private Long productId;
    /** 分类ID */
    @TableField("category_id")
    private Long categoryId;
    /** 商品名称 */
    @TableField("product_name")
    private String productName;
    /** 副标题 */
    @TableField("subtitle")
    private String subtitle;
    /** 主图 */
    @TableField("cover_image")
    private String coverImage;
    /** 图集 */
    @TableField("images")
    private String images;
    /** 售价 */
    @TableField("price")
    private BigDecimal price;
    /** 原价 */
    @TableField("original_price")
    private BigDecimal originalPrice;
    /** 库存 */
    @TableField("stock")
    private Integer stock;
    /** 销量 */
    @TableField("sales")
    private Integer sales;
    /** 单位 */
    @TableField("unit")
    private String unit;
    /** 产地 */
    @TableField("origin")
    private String origin;
    /** 详情 */
    @TableField("description")
    private String description;
    /** 状态 */
    @TableField("status")
    private Integer status;
    /** 创建时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
    /** 更新时间 */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
