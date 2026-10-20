package com.baidu.controller;


import com.baidu.pojo.Order;
import com.baidu.service.impl.OrderServiceImpl;
import com.baidu.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-09
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderServiceImpl orderService;

    //保存数据并且把保存的数据返回
    @PostMapping("/addOrder")
    public Order addOrder(@RequestBody Order order){
        System.out.println("-------addOrder---------order:"+order);
        boolean flag = orderService.save(order);

        return order;
    }

    //根据多个oid的获取订单信息
    @PostMapping("/getOrderByOids")
    public List<Order> getOrderByOids(@RequestParam("oids[]") Integer oids[]){
        System.out.println("---------getOrderByOids--------oids:"+oids);
        List<Order> list = orderService.listByIds(List.of(oids));
        return list;
    }

    //订单发货
    @PostMapping("/editOrder/{oid}")
    public boolean editOrder(@PathVariable("oid") Integer oid){
        System.out.println("-----------editOrder---------oid:"+oid);
        Order order = orderService.getById(oid);
        order.setGoodsStatus("已发货");

        boolean flag = orderService.updateById(order);
        return flag;
    }

    //确认收货
    @PostMapping("/receiveOrder/{oid}")
    public boolean receiveOrder(@PathVariable("oid") Integer oid){
        System.out.println("-----------receiveOrder---------oid:"+oid);
        Order order = orderService.getById(oid);
        order.setGoodsStatus("已收货");
        order.setStatus("已完成");

        boolean flag = orderService.updateById(order);
        return flag;
    }

    //支付成功，更新订单状态和时间
    @PostMapping("/getOrderById")
    public boolean getOrderById(@RequestBody Order order){
        System.out.println("-----------getOrderById---------order:"+order);
        boolean flag = orderService.updateById(order);
        return flag;
    }

    //批量删除订单
    @PostMapping("/batchDelOrder")
    public boolean batchDelOrder(@RequestParam("oids[]") Integer oids[]){
        System.out.println("--------batchDelOrder-------oids:"+oids);
        boolean flag = orderService.removeByIds(List.of(oids));
        return flag;
    }


    //通过id删除订单
    @PostMapping("/deleteOrderByOid/{oid}")
    public boolean deleteOrderByOid(@PathVariable("oid") Integer oid){
        System.out.println("-------deleteOrderByOid-------oid:"+oid);
        boolean flag = orderService.removeById(oid);
        return flag;
    }

    @PostMapping("/getOrderByUid")
    public PageResult<Order> getOrderByUid(@RequestBody Map<String,Object> map){
        System.out.println("-------getAllOrder--------");
        System.out.println("map:"+map);
        Integer currentPage = (Integer)map.get("currentPage");
        Integer limit =(Integer) map.get("limit");
        Integer uid = (Integer) map.get("uid");
        PageResult<Order> pageResult = orderService.getOrderByUid(currentPage, limit, uid);
        return pageResult;
    }

    @PostMapping("/getAllOrder")
    public PageResult<Order> getAllOrder(@RequestBody Map<String,Object> map){
        System.out.println("-------getAllOrder--------");
        System.out.println("map:"+map);
        Integer currentPage = (Integer)map.get("currentPage");

        Integer limit =(Integer) map.get("limit");
        System.out.println("--------------currentPage:"+currentPage);
        PageResult<Order> pageResult = orderService.getAllOrder(currentPage, limit);
        return pageResult;
    }

}
