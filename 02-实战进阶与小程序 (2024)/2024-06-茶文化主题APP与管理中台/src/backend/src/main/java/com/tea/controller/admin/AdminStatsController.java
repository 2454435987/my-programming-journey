package com.tea.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.common.Result;
import com.tea.entity.*;
import com.tea.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 数据统计
 */
@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    @Autowired private AppUserMapper userMapper;
    @Autowired private OrdersMapper ordersMapper;
    @Autowired private TeaProductMapper productMapper;
    @Autowired private TeaKnowledgeMapper knowledgeMapper;
    @Autowired private CommunityPostMapper postMapper;
    @Autowired private TeaActivityMapper activityMapper;
    @Autowired private JdbcTemplate jdbcTemplate;

    /** 首页统计卡片 */
    @GetMapping("/overview")
    public Result<Map<String, Object>> overview() {
        Map<String, Object> data = new HashMap<>();
        data.put("userCount", userMapper.selectCount(null));
        data.put("orderCount", ordersMapper.selectCount(null));
        data.put("productCount", productMapper.selectCount(null));
        data.put("knowledgeCount", knowledgeMapper.selectCount(null));
        data.put("postCount", postMapper.selectCount(null));
        data.put("activityCount", activityMapper.selectCount(null));
        // 销售总额
        List<Orders> paid = ordersMapper.selectList(new LambdaQueryWrapper<Orders>()
                .in(Orders::getStatus, 1, 2, 3));
        double totalSales = paid.stream().mapToDouble(o ->
                o.getPayAmount() == null ? 0 : o.getPayAmount().doubleValue()).sum();
        data.put("totalSales", totalSales);
        // 今日新增用户
        data.put("todayNewUsers", jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM app_user WHERE DATE(created_at)=CURDATE()", Integer.class));
        return Result.ok(data);
    }

    /** 近7天订单趋势 */
    @GetMapping("/order-trend")
    public Result<List<Map<String, Object>>> orderTrend() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT DATE(created_at) AS d, COUNT(*) AS cnt, IFNULL(SUM(pay_amount),0) AS amount " +
                "FROM orders WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
                "GROUP BY DATE(created_at) ORDER BY d");
        return Result.ok(rows);
    }

    /** 订单状态分布 */
    @GetMapping("/order-status")
    public Result<List<Map<String, Object>>> orderStatus() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT status, COUNT(*) AS cnt FROM orders GROUP BY status");
        return Result.ok(rows);
    }

    /** 商品销量TOP */
    @GetMapping("/product-top")
    public Result<List<TeaProduct>> productTop() {
        return Result.ok(productMapper.selectList(new LambdaQueryWrapper<TeaProduct>()
                .orderByDesc(TeaProduct::getSales).last("LIMIT 8")));
    }

    /** 用户注册趋势（近7天） */
    @GetMapping("/user-trend")
    public Result<List<Map<String, Object>>> userTrend() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT DATE(created_at) AS d, COUNT(*) AS cnt FROM app_user " +
                "WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
                "GROUP BY DATE(created_at) ORDER BY d");
        return Result.ok(rows);
    }
}
