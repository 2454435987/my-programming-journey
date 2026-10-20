package com.baidu.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

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
@TableName("cart")
@NoArgsConstructor
@AllArgsConstructor
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车ID
     */
    @TableId(value = "cid", type = IdType.AUTO)
    private Integer cid;

    /**
     * 商品ID
     */
    private Integer pid;

    /**
     * 商品数量
     */
    private Integer pquantity;

    /**
     * 用户ID
     */
    private Integer uid;


    //配置购物车与用户的一对一关联关系
    @TableField(exist = false)
    private User user;

    //配置购物车与商品的一对一关联关系
    @TableField(exist = false)
    private Product product;



}
