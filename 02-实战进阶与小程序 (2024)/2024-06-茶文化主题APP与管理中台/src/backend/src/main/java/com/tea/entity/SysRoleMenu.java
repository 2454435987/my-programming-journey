package com.tea.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色菜单关联实体
 * 表名：sys_role_menu
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenu {

    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 角色ID */
    @TableField("role_id")
    private Long roleId;
    /** 菜单ID */
    @TableField("menu_id")
    private Long menuId;
}
