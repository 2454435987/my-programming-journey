package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * App用户实体
 * 表名：app_user
 */
@Data
@TableName("app_user")
public class AppUser {

    /** 用户ID */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    /** 用户名 */
    @TableField("username")
    private String username;
    /** 密码 */
    @TableField("password")
    private String password;
    /** 昵称 */
    @TableField("nickname")
    private String nickname;
    /** 头像 */
    @TableField("avatar")
    private String avatar;
    /** 性别 */
    @TableField("gender")
    private Integer gender;
    /** 手机号 */
    @TableField("phone")
    private String phone;
    /** 邮箱 */
    @TableField("email")
    private String email;
    /** 积分 */
    @TableField("points")
    private Integer points;
    /** 会员等级 */
    @TableField("level")
    private Integer level;
    /** 状态 */
    @TableField("status")
    private Integer status;
    /** 个性签名 */
    @TableField("signature")
    private String signature;
    /** 注册时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
    /** 更新时间 */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
