package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 内容点赞实体
 * 表名：content_like
 */
@Data
@TableName("content_like")
public class ContentLike {

    /** 点赞ID */
    @TableId(value = "like_id", type = IdType.AUTO)
    private Long likeId;
    /** 用户ID */
    @TableField("user_id")
    private Long userId;
    /** 目标ID */
    @TableField("target_id")
    private Long targetId;
    /** 类型 */
    @TableField("target_type")
    private Integer targetType;
    /** 点赞时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
