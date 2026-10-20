package com.baidu.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_order")
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "oid", type = IdType.AUTO)
    private Integer oid;

    /**
     * 订单总金额
     */
    private BigDecimal totalPrice;

    /**
     * 买家ID
     */
    private Integer uid;

    /**
     * 商家ID
     */
    private Integer merchantId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**
     * 支付状态
     */
    private String status;

    /**
     * 物流状态
     */
    @TableField("goodsStatus")
    private String goodsStatus;

    /**
     * 商品ID
     */
    @TableField("pid")
    private Integer pid;

    /**
     * 购买数量
     */
    @TableField("quantity")
    private Integer quantity;

    //配置订单与买家的多对一关联关系
    @TableField(exist = false)
    User user;

    //配置订单与卖家的多对一关联关系
    @TableField(exist = false)
    User merchant;

    //配置订单与商品的多对一关联关系
    @TableField(exist = false)
    Product product;
}
