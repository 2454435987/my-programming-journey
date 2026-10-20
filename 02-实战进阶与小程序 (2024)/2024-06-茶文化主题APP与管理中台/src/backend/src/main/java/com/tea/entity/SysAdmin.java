package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统管理员实体
 * 表名：sys_admin
 */
@Data
@TableName("sys_admin")
public class SysAdmin {

    /** 管理员ID */
    @TableId(value = "admin_id", type = IdType.AUTO)
    private Long adminId;
    /** 登录账号 */
    @TableField("username")
    private String username;
    /** 密码 */
    @TableField("password")
    private String password;
    /** 姓名 */
    @TableField("real_name")
    private String realName;
    /** 头像 */
    @TableField("avatar")
    private String avatar;
    /** 邮箱 */
    @TableField("email")
    private String email;
    /** 手机号 */
    @TableField("phone")
    private String phone;
    /** 状态 */
    @TableField("status")
    private Integer status;
    /** 最后登录时间 */
    @TableField("last_login")
    private LocalDateTime lastLogin;
    /** 创建时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
    /** 更新时间 */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
