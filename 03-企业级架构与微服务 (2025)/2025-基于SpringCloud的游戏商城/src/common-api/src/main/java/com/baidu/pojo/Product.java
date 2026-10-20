package com.baidu.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;

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
@TableName("t_product")
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 商品ID
     */
    @TableId(value = "pid", type = IdType.AUTO)
    private Integer pid;

    /**
     * 商品名称
     */
    private String pname;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品图片
     */
    private String pfile;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 商家ID
     */
    private Integer uid;

    /**
     * 商品状态 1上架 0下架
     */
    private Integer status;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 审核状态：待审核、已审核、审核拒绝
     */
    private String auditStatus;

    @TableField(exist = false)
    private Long collectCount = 0L;

    @TableField(exist = false)
    private Boolean isCollected = false;



}
