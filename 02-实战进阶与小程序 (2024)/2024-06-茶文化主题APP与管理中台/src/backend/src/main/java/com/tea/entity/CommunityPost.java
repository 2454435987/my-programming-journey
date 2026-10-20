package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 社区帖子实体
 * 表名：community_post
 */
@Data
@TableName("community_post")
public class CommunityPost {

    /** 帖子ID */
    @TableId(value = "post_id", type = IdType.AUTO)
    private Long postId;
    /** 用户ID */
    @TableField("user_id")
    private Long userId;
    /** 标题 */
    @TableField("title")
    private String title;
    /** 正文 */
    @TableField("content")
    private String content;
    /** 图片 */
    @TableField("images")
    private String images;
    /** 话题 */
    @TableField("topic")
    private String topic;
    /** 浏览量 */
    @TableField("view_count")
    private Integer viewCount;
    /** 点赞量 */
    @TableField("like_count")
    private Integer likeCount;
    /** 评论数 */
    @TableField("comment_count")
    private Integer commentCount;
    /** 是否置顶 */
    @TableField("is_top")
    private Integer isTop;
    /** 是否精华 */
    @TableField("is_essence")
    private Integer isEssence;
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
