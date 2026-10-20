package com.baidu.dao;

import com.baidu.pojo.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface ProductMapper extends BaseMapper<Product> {

    //根据商品名称进行模糊分页查询和普通分页查询
//    public List<Product> getProductPageByPname(@Param("pname")String pname,@Param("start")Integer start,@Param("pageSize")Integer pageSize);


}
