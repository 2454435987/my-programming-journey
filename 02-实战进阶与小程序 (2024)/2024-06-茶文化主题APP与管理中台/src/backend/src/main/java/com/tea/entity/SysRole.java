package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色实体
 * 表名：sys_role
 */
@Data
@TableName("sys_role")
public class SysRole {

    /** 角色ID */
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;
    /** 角色编码 */
    @TableField("role_code")
    private String roleCode;
    /** 角色名称 */
    @TableField("role_name")
    private String roleName;
    /** 描述 */
    @TableField("description")
    private String description;
    /** 状态 */
    @TableField("status")
    private Integer status;
    /** 创建时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
