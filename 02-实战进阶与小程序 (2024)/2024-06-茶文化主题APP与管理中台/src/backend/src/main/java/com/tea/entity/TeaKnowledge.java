package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 茶文化知识实体
 * 表名：tea_knowledge
 */
@Data
@TableName("tea_knowledge")
public class TeaKnowledge {

    /** 知识ID */
    @TableId(value = "knowledge_id", type = IdType.AUTO)
    private Long knowledgeId;
    /** 分类ID */
    @TableField("category_id")
    private Long categoryId;
    /** 标题 */
    @TableField("title")
    private String title;
    /** 摘要 */
    @TableField("summary")
    private String summary;
    /** 正文 */
    @TableField("content")
    private String content;
    /** 封面图 */
    @TableField("cover_image")
    private String coverImage;
    /** 作者 */
    @TableField("author")
    private String author;
    /** 来源 */
    @TableField("source")
    private String source;
    /** 浏览量 */
    @TableField("view_count")
    private Integer viewCount;
    /** 点赞量 */
    @TableField("like_count")
    private Integer likeCount;
    /** 是否置顶 */
    @TableField("is_top")
    private Integer isTop;
    /** 状态 */
    @TableField("status")
    private Integer status;
    /** 发布时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
    /** 更新时间 */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
