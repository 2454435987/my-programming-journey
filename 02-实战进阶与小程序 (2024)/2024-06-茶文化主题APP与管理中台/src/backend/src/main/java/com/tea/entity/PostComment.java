package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 帖子评论实体
 * 表名：post_comment
 */
@Data
@TableName("post_comment")
public class PostComment {

    /** 评论ID */
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;
    /** 帖子ID */
    @TableField("post_id")
    private Long postId;
    /** 用户ID */
    @TableField("user_id")
    private Long userId;
    /** 父评论ID */
    @TableField("parent_id")
    private Long parentId;
    /** 评论内容 */
    @TableField("content")
    private String content;
    /** 点赞数 */
    @TableField("like_count")
    private Integer likeCount;
    /** 状态 */
    @TableField("status")
    private Integer status;
    /** 评论时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
