package com.water.delivery.vo;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class CustomerVO {
    private Long id;
    private String customerNo;
    private String name;
    private String phone;
    private Long regionId;
    private String regionName;
    private String addressDetail;
    private Integer floorNum;
    private Integer hasElevator;
    private Integer customerType;
    private Integer unreturnedBarrels;
    private Integer warningThreshold;
    private Integer totalBalance; // 该客户所有品牌水票总余票
    private Boolean isWarning;    // 是否处于告急预警状态
    private Date createTime;
    private List<TicketAccountVO> accounts; // 各品牌水票明细

    // 兼容前端不同模块属性取名
    public String getCustomerName() {
        return name;
    }

    public String getAddress() {
        return addressDetail;
    }

    public Integer getFloorNumber() {
        return floorNum;
    }
}
