package com.baidu.dao;

import com.baidu.pojo.Cart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {
    //根据用户主键和商品名称进行模糊分页查询和普通分页查询
    public List<Cart> getCartPageByUidAndPname(@Param("uid")Integer uid,
                                               @Param("pname") String pname,
                                               @Param("start") Integer start,
                                               @Param("pageSize") Integer pageSize
    );

    //商品名称进行模糊分页查询和普通分页查询
    List<Cart> getAllCartPage(@Param("pname") String pname, @Param("start") Integer start, @Param("pageSize") Integer
            pageSize);

    //计算普通分页与模糊分页的总记录数
    public int myCount(@Param("uid") Integer uid,@Param("pname") String pname);

}
