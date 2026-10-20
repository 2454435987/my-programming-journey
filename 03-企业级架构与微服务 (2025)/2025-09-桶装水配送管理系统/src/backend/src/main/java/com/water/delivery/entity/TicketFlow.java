package com.water.delivery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_ticket_flow")
public class TicketFlow implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String flowNo;
    private Long customerId;
    private Long typeId;
    private Integer bizType; // 1=充值发放, 2=配送核销, 3=作废扣减, 4=退票回收
    private Integer changeAmount;
    private Integer beforeBalance;
    private Integer afterBalance;
    private String refOrderNo;
    private Long operatorId;
    private String remark;
    private Date createTime;
}
