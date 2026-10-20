package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 菜单权限实体
 * 表名：sys_menu
 */
@Data
@TableName("sys_menu")
public class SysMenu {

    /** 菜单ID */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;
    /** 父菜单ID */
    @TableField("parent_id")
    private Long parentId;
    /** 菜单名称 */
    @TableField("menu_name")
    private String menuName;
    /** 路由路径 */
    @TableField("path")
    private String path;
    /** 权限标识 */
    @TableField("perms")
    private String perms;
    /** 类型 */
    @TableField("menu_type")
    private Integer menuType;
    /** 排序 */
    @TableField("sort_order")
    private Integer sortOrder;
    /** 图标 */
    @TableField("icon")
    private String icon;
    /** 是否可见 */
    @TableField("visible")
    private Integer visible;
    /** 创建时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
