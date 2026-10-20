package com.baidu.service;

import com.baidu.pojo.Cart;
import com.baidu.utils.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
public interface CartService extends IService<Cart> {


    //根据uid和pid查询购物车信息
    public Cart getCartByUidAndPid(int uid,int pid);

    //根据用户主键和商品名称进行模糊分页查询和普通分页查询
    //根据用户主键和商品名称进行模糊分页查询和普通分页查询
    public PageResult<Cart> getCartPageByUidAndPname(Integer uid, String
            pname, Integer currentPage, Integer pageSize);

}
