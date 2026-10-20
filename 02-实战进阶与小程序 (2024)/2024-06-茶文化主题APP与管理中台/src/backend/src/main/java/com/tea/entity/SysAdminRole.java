package com.tea.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 管理员角色关联实体
 * 表名：sys_admin_role
 */
@Data
@TableName("sys_admin_role")
public class SysAdminRole {

    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 管理员ID */
    @TableField("admin_id")
    private Long adminId;
    /** 角色ID */
    @TableField("role_id")
    private Long roleId;
}
