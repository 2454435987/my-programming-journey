package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 操作日志实体
 * 表名：sys_operation_log
 */
@Data
@TableName("sys_operation_log")
public class SysOperationLog {

    /** 日志ID */
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;
    /** 操作人ID */
    @TableField("admin_id")
    private Long adminId;
    /** 操作人 */
    @TableField("admin_name")
    private String adminName;
    /** 操作内容 */
    @TableField("operation")
    private String operation;
    /** 请求方法 */
    @TableField("method")
    private String method;
    /** 请求参数 */
    @TableField("params")
    private String params;
    /** IP */
    @TableField("ip")
    private String ip;
    /** 状态 */
    @TableField("status")
    private Integer status;
    /** 操作时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
