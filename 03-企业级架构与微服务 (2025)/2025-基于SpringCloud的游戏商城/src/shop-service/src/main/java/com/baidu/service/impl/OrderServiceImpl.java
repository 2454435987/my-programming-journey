package com.baidu.service.impl;

import com.baidu.dao.UserMapper;
import com.baidu.pojo.Order;
import com.baidu.dao.OrderMapper;
import com.baidu.pojo.User;
import com.baidu.service.OrderService;
import com.baidu.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    com.baidu.dao.ProductMapper productMapper;

    public PageResult<Order> getOrderByUid(Integer currentPage, Integer pageSize,Integer uid){
        System.out.println("--------getOrderByUid-------");
        Page<Order> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(currentPage);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        // 使用 or() 方法，意思是：买家是我（uid） 或者 卖家是我（merchant_id） 的订单都查出来
        queryWrapper.eq("uid",uid).or().eq("merchant_id",uid);
        Page<Order> page1 = orderMapper.selectPage(page,queryWrapper);
        for (Order record : page1.getRecords()) {
            User user = userMapper.selectById(record.getUid());
            record.setUser(user);
            User merchant = userMapper.selectById(record.getMerchantId());
            record.setMerchant(merchant);
            if (record.getPid() != null) {
                com.baidu.pojo.Product product = productMapper.selectById(record.getPid());
                record.setProduct(product);
            }
        }
        long total =  orderMapper.selectCount(queryWrapper);
        int count = (int) total;
        System.out.println("-------------count--------:"+count);
        PageResult<Order> pageResult = new PageResult<>();
        pageResult.setCount(count);
        pageResult.setCode(0);
        pageResult.setMsg("普通用户订单分页查询成功");
        pageResult.setData(page1.getRecords());
        return pageResult;

    }
    public PageResult<Order> getAllOrder(Integer pageCurrent, Integer pageSize){
        System.out.println("---------getAllOrder-------");
        Page<Order> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageCurrent);
        Page<Order> page1 = orderMapper.selectPage(page, null);
        for (Order record : page1.getRecords()) {
            User user = userMapper.selectById(record.getUid());
            record.setUser(user);
            User merchant = userMapper.selectById(record.getMerchantId());
            record.setMerchant(merchant);
            if (record.getPid() != null) {
                com.baidu.pojo.Product product = productMapper.selectById(record.getPid());
                record.setProduct(product);
            }
        }
        System.out.println("page1:"+ page1.getRecords());



        PageResult pageResult = new PageResult();
        long total = orderMapper.selectCount(null);
        int count = (int) total;
        System.out.println("-------------count---------:"+count);
        pageResult.setCount(count);
        pageResult.setCode(0);
        pageResult.setMsg("订单分页查询成功");
        pageResult.setData(page1.getRecords());
        return pageResult;
    }
}
