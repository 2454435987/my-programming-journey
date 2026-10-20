package com.tea.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.common.BizException;
import com.tea.common.PageResult;
import com.tea.common.Result;
import com.tea.entity.OrderItem;
import com.tea.entity.Orders;
import com.tea.entity.TeaProduct;
import com.tea.mapper.OrderItemMapper;
import com.tea.mapper.OrdersMapper;
import com.tea.mapper.TeaProductMapper;
import com.tea.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品与订单管理
 */
@RestController
@RequestMapping("/api/admin")
public class AdminMallController {

    @Autowired private TeaProductMapper productMapper;
    @Autowired private OrdersMapper ordersMapper;
    @Autowired private OrderItemMapper orderItemMapper;
    @Autowired private OrderService orderService;

    // ========== 商品 ==========
    @GetMapping("/products")
    public Result<PageResult<TeaProduct>> productList(
            @RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String keyword, @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<TeaProduct> qw = new LambdaQueryWrapper<TeaProduct>()
                .like(keyword != null && !keyword.isEmpty(), TeaProduct::getProductName, keyword)
                .eq(status != null, TeaProduct::getStatus, status)
                .orderByDesc(TeaProduct::getCreatedAt);
        Page<TeaProduct> p = productMapper.selectPage(new Page<>(page, size), qw);
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    @PostMapping("/products")
    public Result<TeaProduct> addProduct(@RequestBody TeaProduct body) {
        body.setProductId(null);
        body.setSales(0);
        body.setCreatedAt(LocalDateTime.now());
        productMapper.insert(body);
        return Result.ok(body);
    }

    @PutMapping("/products/{id}")
    public Result<Void> updateProduct(@PathVariable Long id, @RequestBody TeaProduct body) {
        body.setProductId(id);
        body.setUpdatedAt(LocalDateTime.now());
        productMapper.updateById(body);
        return Result.ok();
    }

    @DeleteMapping("/products/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        productMapper.deleteById(id);
        return Result.ok();
    }

    // ========== 订单 ==========
    @GetMapping("/orders")
    public Result<PageResult<Orders>> orderList(
            @RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String keyword, @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<Orders> qw = new LambdaQueryWrapper<Orders>()
                .and(keyword != null && !keyword.isEmpty(), w ->
                        w.like(Orders::getOrderNo, keyword).or().like(Orders::getReceiver, keyword))
                .eq(status != null, Orders::getStatus, status)
                .orderByDesc(Orders::getCreatedAt);
        Page<Orders> p = ordersMapper.selectPage(new Page<>(page, size), qw);
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    @GetMapping("/orders/{id}")
    public Result<Map<String, Object>> orderDetail(@PathVariable Long id) {
        Orders order = ordersMapper.selectById(id);
        if (order == null) throw new BizException(404, "订单不存在");
        Map<String, Object> data = new HashMap<>();
        data.put("order", order);
        data.put("items", orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderId, id)));
        return Result.ok(data);
    }

    @PutMapping("/orders/{id}/ship")
    public Result<Void> shipOrder(@PathVariable Long id) {
        orderService.shipOrder(id);
        return Result.ok();
    }
}
