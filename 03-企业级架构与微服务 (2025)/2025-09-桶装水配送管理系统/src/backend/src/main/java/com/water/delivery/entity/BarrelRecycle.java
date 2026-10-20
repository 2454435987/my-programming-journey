package com.water.delivery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_barrel_recycle")
public class BarrelRecycle implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Long customerId;
    private Long courierId;
    private Integer deliveredBarrels;
    private Integer recycledBarrels;
    private Integer barrelDiff;
    private String aiInspectStatus; // PASSED, NEED_CLEAN, SCRAP
    private String aiInspectDesc;
    private BigDecimal suggestDeductDeposit;
    private String inspectImageUrl;
    private Date recycleTime;
}
