package com.tea.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 消息通知实体
 * 表名：message_notice
 */
@Data
@TableName("message_notice")
public class MessageNotice {

    /** 消息ID */
    @TableId(value = "message_id", type = IdType.AUTO)
    private Long messageId;
    /** 接收用户ID */
    @TableField("user_id")
    private Long userId;
    /** 消息标题 */
    @TableField("title")
    private String title;
    /** 消息内容 */
    @TableField("content")
    private String content;
    /** 类型 */
    @TableField("msg_type")
    private Integer msgType;
    /** 是否已读 */
    @TableField("is_read")
    private Integer isRead;
    /** 发送时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
