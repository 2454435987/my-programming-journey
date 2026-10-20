package com.baidu.feignservice;

import com.baidu.pojo.Order;
import com.baidu.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "shop-service",path = "/order")
public interface OrderFeignService {

    //保存数据并且把保存的数据返回
    @PostMapping("/addOrder")
    public Order addOrder(@RequestBody Order Order);

    //根据多个oid的获取订单信息
    @PostMapping("/getOrderByOids")
    public List<Order> getOrderByOids(@RequestParam("oids[]") Integer oids[]);

    //订单发货
    @PostMapping("/editOrder/{oid}")
    public boolean editOrder(@PathVariable("oid") Integer oid);

    //确认收货
    @PostMapping("/receiveOrder/{oid}")
    public boolean receiveOrder(@PathVariable("oid") Integer oid);

    //支付订单
    @PostMapping("/getOrderById")
    public boolean getOrderById(@RequestBody Order order);

    //批量删除订单
    @PostMapping("/batchDelOrder")
    public boolean batchDelOrder(@RequestParam("oids[]") Integer oids[]);

    //通过id删除订单
    @PostMapping("/deleteOrderByOid/{oid}")
    public boolean deleteOrderByOid(@PathVariable("oid") Integer oid);

    //获取指定用户订单
    @PostMapping("/getOrderByUid")
    public PageResult<Order> getOrderByUid(@RequestBody Map<String,Object> map);

    //获取所有订单
    @PostMapping("/getAllOrder")
    public PageResult<Order> getAllOrder(Map<String,Object> map);
}
