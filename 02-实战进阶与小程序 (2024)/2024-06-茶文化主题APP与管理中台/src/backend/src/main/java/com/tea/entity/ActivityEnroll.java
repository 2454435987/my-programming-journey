package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 活动报名实体
 * 表名：activity_enroll
 */
@Data
@TableName("activity_enroll")
public class ActivityEnroll {

    /** 报名ID */
    @TableId(value = "enroll_id", type = IdType.AUTO)
    private Long enrollId;
    /** 活动ID */
    @TableField("activity_id")
    private Long activityId;
    /** 用户ID */
    @TableField("user_id")
    private Long userId;
    /** 姓名 */
    @TableField("real_name")
    private String realName;
    /** 手机号 */
    @TableField("phone")
    private String phone;
    /** 备注 */
    @TableField("remark")
    private String remark;
    /** 状态 */
    @TableField("status")
    private Integer status;
    /** 报名时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
