package com.baidu.service;

import com.baidu.pojo.Product;
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
public interface ProductService extends IService<Product> {
    public PageResult<Product> getProductBypage(Integer currentPage,Integer pageSize);

    public PageResult<Product> getProductPageByPname(String pname, Integer start, Integer pageSize);



}
