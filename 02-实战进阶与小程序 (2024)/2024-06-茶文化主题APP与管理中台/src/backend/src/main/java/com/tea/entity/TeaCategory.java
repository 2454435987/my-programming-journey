package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 分类实体
 * 表名：tea_category
 */
@Data
@TableName("tea_category")
public class TeaCategory {

    /** 分类ID */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;
    /** 分类名称 */
    @TableField("category_name")
    private String categoryName;
    /** 类型 */
    @TableField("category_type")
    private Integer categoryType;
    /** 描述 */
    @TableField("description")
    private String description;
    /** 图标 */
    @TableField("icon")
    private String icon;
    /** 排序 */
    @TableField("sort_order")
    private Integer sortOrder;
    /** 状态 */
    @TableField("status")
    private Integer status;
    /** 创建时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
