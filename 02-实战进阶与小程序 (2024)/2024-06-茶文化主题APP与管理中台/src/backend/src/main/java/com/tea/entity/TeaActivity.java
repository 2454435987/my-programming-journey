package com.tea.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 茶事活动实体
 * 表名：tea_activity
 */
@Data
@TableName("tea_activity")
public class TeaActivity {

    /** 活动ID */
    @TableId(value = "activity_id", type = IdType.AUTO)
    private Long activityId;
    /** 活动标题 */
    @TableField("title")
    private String title;
    /** 活动简介 */
    @TableField("summary")
    private String summary;
    /** 活动详情 */
    @TableField("content")
    private String content;
    /** 封面图 */
    @TableField("cover_image")
    private String coverImage;
    /** 活动类型 */
    @TableField("activity_type")
    private String activityType;
    /** 活动地点 */
    @TableField("location")
    private String location;
    /** 开始时间 */
    @TableField("start_time")
    private LocalDateTime startTime;
    /** 结束时间 */
    @TableField("end_time")
    private LocalDateTime endTime;
    /** 报名截止 */
    @TableField("enroll_deadline")
    private LocalDateTime enrollDeadline;
    /** 最大报名人数 */
    @TableField("max_enroll")
    private Integer maxEnroll;
    /** 已报名人数 */
    @TableField("enrolled_count")
    private Integer enrolledCount;
    /** 费用 */
    @TableField("price")
    private BigDecimal price;
    /** 状态 */
    @TableField("status")
    private Integer status;
    /** 创建时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
    /** 更新时间 */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
