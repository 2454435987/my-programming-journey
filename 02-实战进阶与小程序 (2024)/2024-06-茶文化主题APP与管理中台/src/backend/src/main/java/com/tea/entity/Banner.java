package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 轮播运营位实体
 * 表名：banner
 */
@Data
@TableName("banner")
public class Banner {

    /** BannerID */
    @TableId(value = "banner_id", type = IdType.AUTO)
    private Long bannerId;
    /** 标题 */
    @TableField("title")
    private String title;
    /** 图片地址 */
    @TableField("image_url")
    private String imageUrl;
    /** 跳转链接 */
    @TableField("link_url")
    private String linkUrl;
    /** 位置 */
    @TableField("position")
    private String position;
    /** 排序 */
    @TableField("sort_order")
    private Integer sortOrder;
    /** 状态 */
    @TableField("status")
    private Integer status;
    /** 生效时间 */
    @TableField("start_time")
    private LocalDateTime startTime;
    /** 失效时间 */
    @TableField("end_time")
    private LocalDateTime endTime;
    /** 创建时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
