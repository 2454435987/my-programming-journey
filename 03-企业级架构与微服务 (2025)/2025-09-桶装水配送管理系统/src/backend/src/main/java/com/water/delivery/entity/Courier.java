package com.water.delivery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
@TableName("t_courier")
public class Courier implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String courierNo;
    private String name;
    private String phone;
    private Long regionId;
    private String vehicleType;
    private Integer status; // 1=在职, 2=休假, 0=停用离职
    private LocalDate entryDate;
    private String idCard;
    private Date createTime;
}
