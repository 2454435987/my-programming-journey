package com.water.delivery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_ticket_type")
public class TicketType implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String brandName;
    private String specDesc;
    private BigDecimal unitPrice;
    private BigDecimal barrelDeposit;
    private Integer isActive; // 1=在售, 0=停用
    private Date createTime;
}
