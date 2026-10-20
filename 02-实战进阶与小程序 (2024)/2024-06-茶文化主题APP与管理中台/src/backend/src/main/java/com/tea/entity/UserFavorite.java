package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户收藏实体
 * 表名：user_favorite
 */
@Data
@TableName("user_favorite")
public class UserFavorite {

    /** 收藏ID */
    @TableId(value = "favorite_id", type = IdType.AUTO)
    private Long favoriteId;
    /** 用户ID */
    @TableField("user_id")
    private Long userId;
    /** 目标ID */
    @TableField("target_id")
    private Long targetId;
    /** 类型 */
    @TableField("target_type")
    private Integer targetType;
    /** 收藏时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
