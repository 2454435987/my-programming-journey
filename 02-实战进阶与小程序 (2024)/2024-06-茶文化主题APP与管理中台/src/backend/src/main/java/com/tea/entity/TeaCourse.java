package com.tea.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 茶艺教程实体
 * 表名：tea_course
 */
@Data
@TableName("tea_course")
public class TeaCourse {

    /** 教程ID */
    @TableId(value = "course_id", type = IdType.AUTO)
    private Long courseId;
    /** 分类ID */
    @TableField("category_id")
    private Long categoryId;
    /** 标题 */
    @TableField("title")
    private String title;
    /** 简介 */
    @TableField("summary")
    private String summary;
    /** 内容 */
    @TableField("content")
    private String content;
    /** 封面图 */
    @TableField("cover_image")
    private String coverImage;
    /** 视频地址 */
    @TableField("video_url")
    private String videoUrl;
    /** 难度 */
    @TableField("level")
    private Integer level;
    /** 时长 */
    @TableField("duration")
    private Integer duration;
    /** 浏览量 */
    @TableField("view_count")
    private Integer viewCount;
    /** 点赞量 */
    @TableField("like_count")
    private Integer likeCount;
    /** 是否免费 */
    @TableField("is_free")
    private Integer isFree;
    /** 价格 */
    @TableField("price")
    private BigDecimal price;
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
