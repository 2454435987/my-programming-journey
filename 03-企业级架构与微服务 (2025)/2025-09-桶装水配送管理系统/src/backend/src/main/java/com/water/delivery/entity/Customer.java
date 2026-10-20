package com.water.delivery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_customer")
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String customerNo;
    private String name;
    private String phone;
    private Long regionId;
    private String addressDetail;
    private Integer floorNum;
    private Integer hasElevator; // 1=有电梯, 0=无电梯步梯
    private Integer customerType; // 1=家庭散客, 2=企业大客户
    private Integer unreturnedBarrels; // 当前累计未还空桶欠数
    private Integer warningThreshold; // 余票预警阈值
    private Date createTime;
}
