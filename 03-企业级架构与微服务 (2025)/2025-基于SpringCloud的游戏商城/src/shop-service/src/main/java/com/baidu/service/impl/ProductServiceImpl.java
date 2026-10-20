package com.baidu.service.impl;

import com.baidu.pojo.Product;
import com.baidu.dao.ProductMapper;
import com.baidu.pojo.User;
import com.baidu.service.ProductService;
import com.baidu.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    //实现模糊分页查询和分页查询
    public PageResult<Product> getProductPageByPname(String pname, Integer currentPage, Integer pageSize) {
        System.out.println("----------getProductPageByPname---------");
        Page<Product> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        if(pname!=null){
            queryWrapper.like("pname",pname);
        }
        //执行分页查询
        Page page1 = productMapper.selectPage(page, queryWrapper);
        PageResult<Product> pageResult = new PageResult<>();
        pageResult.setCode(0);
        pageResult.setMsg("分页查询商品成功");
        //查询商品信息中记录数
        long total = productMapper.selectCount(queryWrapper);
        int count=(int)total;
        pageResult.setCount(count);
        //获取分页查询的数据
        pageResult.setData(page1.getRecords());

        return pageResult;
    }
    public PageResult<Product> getProductPageByPnameAndUid(String pname, Integer currentPage, Integer pageSize, Integer uid){
        System.out.println("--------getProductPageByPnameAndUid-------");
        Page<Product> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(currentPage);
        QueryWrapper queryWrapper = new QueryWrapper();
        if(uid!=null){
            queryWrapper.eq("uid",uid);
            queryWrapper.eq("status", 1); //只展示挂售中(status=1)的商品
        }
        if(pname!=null){
            queryWrapper.like("pname",pname);
        }
        Page<Product> page1 = productMapper.selectPage(page,queryWrapper);

        PageResult pageResult = new PageResult();
        long total = productMapper.selectCount(queryWrapper);
        pageResult.setCount((int)total);
        pageResult.setCode(0);
        pageResult.setMsg("分页查询商品成功");
        pageResult.setData( page1.getRecords());
        return pageResult;
    }


    //根据当前页和页面容量，实现分页查询
    public PageResult<Product> getProductBypage(Integer currentPage, Integer pageSize){
        //创建Page<Product>对象，保存当前页和页面容量
        Page<Product> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);

        //创建PageResult<Product>对象，保存查询结果
        PageResult<Product> pageResult = new PageResult<>();
        //设置code和msg
        pageResult.setCode(0);
        pageResult.setMsg("分页查询商品成功");

        //查询商品信息的总记录数
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("audit_status", "已审核");
        queryWrapper.eq("status", 1);
        long total = productMapper.selectCount(queryWrapper);

        int count= (int)total;
        //将总记录数保存到PageResult<Product>对象中
        pageResult.setCount(count);
        //执行分页查询
        IPage<Product> iPage = productMapper.selectPage(page, queryWrapper);
        //从Ipage对象中获取数据的集合
        List<Product> list = iPage.getRecords();
        //将商品集合保存到PageResult<Product>对象中
        pageResult.setData(list);
        return  pageResult;
    }




}
