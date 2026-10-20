package com.water.delivery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_ticket_account")
public class TicketAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private Long typeId;
    private Integer balance;
    private Integer totalRecharge;
    private Integer totalConsumed;

    @Version
    private Integer version; // 乐观锁版本号

    private Date updateTime;
}
