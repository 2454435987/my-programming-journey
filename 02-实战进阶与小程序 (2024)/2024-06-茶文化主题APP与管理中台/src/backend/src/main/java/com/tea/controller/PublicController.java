package com.tea.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.common.PageResult;
import com.tea.common.Result;
import com.tea.entity.*;
import com.tea.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公开接口：首页聚合、知识、商品、教程、社区、活动
 */
@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired private BannerMapper bannerMapper;
    @Autowired private TeaKnowledgeMapper knowledgeMapper;
    @Autowired private TeaProductMapper productMapper;
    @Autowired private TeaCourseMapper courseMapper;
    @Autowired private CommunityPostMapper postMapper;
    @Autowired private TeaActivityMapper activityMapper;
    @Autowired private TeaCategoryMapper categoryMapper;

    /** 首页聚合数据 */
    @GetMapping("/home")
    public Result<Map<String, Object>> home() {
        Map<String, Object> data = new HashMap<>();
        data.put("banners", bannerMapper.selectList(new LambdaQueryWrapper<Banner>()
                .eq(Banner::getStatus, 1).orderByAsc(Banner::getSortOrder)));
        data.put("hotProducts", productMapper.selectList(new LambdaQueryWrapper<TeaProduct>()
                .eq(TeaProduct::getStatus, 1).orderByDesc(TeaProduct::getSales).last("LIMIT 6")));
        data.put("newKnowledge", knowledgeMapper.selectList(new LambdaQueryWrapper<TeaKnowledge>()
                .eq(TeaKnowledge::getStatus, 1).orderByDesc(TeaKnowledge::getIsTop)
                .orderByDesc(TeaKnowledge::getCreatedAt).last("LIMIT 4")));
        data.put("hotCourses", courseMapper.selectList(new LambdaQueryWrapper<TeaCourse>()
                .eq(TeaCourse::getStatus, 1).orderByDesc(TeaCourse::getViewCount).last("LIMIT 4")));
        data.put("activities", activityMapper.selectList(new LambdaQueryWrapper<TeaActivity>()
                .eq(TeaActivity::getStatus, 1).orderByDesc(TeaActivity::getStartTime).last("LIMIT 3")));
        data.put("hotPosts", postMapper.selectList(new LambdaQueryWrapper<CommunityPost>()
                .eq(CommunityPost::getStatus, 1).orderByDesc(CommunityPost::getIsTop)
                .orderByDesc(CommunityPost::getLikeCount).last("LIMIT 4")));
        return Result.ok(data);
    }

    /** 分类列表 */
    @GetMapping("/categories")
    public Result<List<TeaCategory>> categories(@RequestParam(required = false) Integer type) {
        LambdaQueryWrapper<TeaCategory> qw = new LambdaQueryWrapper<TeaCategory>()
                .eq(TeaCategory::getStatus, 1).orderByAsc(TeaCategory::getSortOrder);
        if (type != null) qw.eq(TeaCategory::getCategoryType, type);
        return Result.ok(categoryMapper.selectList(qw));
    }

    /** 知识分页 */
    @GetMapping("/knowledge")
    public Result<PageResult<TeaKnowledge>> knowledge(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<TeaKnowledge> qw = new LambdaQueryWrapper<TeaKnowledge>()
                .eq(TeaKnowledge::getStatus, 1)
                .eq(categoryId != null, TeaKnowledge::getCategoryId, categoryId)
                .and(keyword != null && !keyword.isEmpty(), w ->
                        w.like(TeaKnowledge::getTitle, keyword).or().like(TeaKnowledge::getSummary, keyword))
                .orderByDesc(TeaKnowledge::getIsTop).orderByDesc(TeaKnowledge::getCreatedAt);
        Page<TeaKnowledge> p = knowledgeMapper.selectPage(new Page<>(page, size), qw);
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    /** 知识详情（含浏览量+1） */
    @GetMapping("/knowledge/{id}")
    public Result<TeaKnowledge> knowledgeDetail(@PathVariable Long id) {
        TeaKnowledge k = knowledgeMapper.selectById(id);
        if (k != null) {
            k.setViewCount((k.getViewCount() == null ? 0 : k.getViewCount()) + 1);
            knowledgeMapper.updateById(k);
        }
        return Result.ok(k);
    }

    /** 商品分页 */
    @GetMapping("/products")
    public Result<PageResult<TeaProduct>> products(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort) {
        LambdaQueryWrapper<TeaProduct> qw = new LambdaQueryWrapper<TeaProduct>()
                .eq(TeaProduct::getStatus, 1)
                .eq(categoryId != null, TeaProduct::getCategoryId, categoryId)
                .and(keyword != null && !keyword.isEmpty(), w ->
                        w.like(TeaProduct::getProductName, keyword).or().like(TeaProduct::getSubtitle, keyword));
        if ("price_asc".equals(sort)) qw.orderByAsc(TeaProduct::getPrice);
        else if ("price_desc".equals(sort)) qw.orderByDesc(TeaProduct::getPrice);
        else if ("sales".equals(sort)) qw.orderByDesc(TeaProduct::getSales);
        else qw.orderByDesc(TeaProduct::getCreatedAt);
        Page<TeaProduct> p = productMapper.selectPage(new Page<>(page, size), qw);
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    /** 商品详情 */
    @GetMapping("/products/{id}")
    public Result<TeaProduct> productDetail(@PathVariable Long id) {
        return Result.ok(productMapper.selectById(id));
    }

    /** 教程分页 */
    @GetMapping("/courses")
    public Result<PageResult<TeaCourse>> courses(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer level) {
        LambdaQueryWrapper<TeaCourse> qw = new LambdaQueryWrapper<TeaCourse>()
                .eq(TeaCourse::getStatus, 1)
                .eq(categoryId != null, TeaCourse::getCategoryId, categoryId)
                .eq(level != null, TeaCourse::getLevel, level)
                .orderByDesc(TeaCourse::getViewCount);
        Page<TeaCourse> p = courseMapper.selectPage(new Page<>(page, size), qw);
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    /** 教程详情 */
    @GetMapping("/courses/{id}")
    public Result<TeaCourse> courseDetail(@PathVariable Long id) {
        TeaCourse c = courseMapper.selectById(id);
        if (c != null) {
            c.setViewCount((c.getViewCount() == null ? 0 : c.getViewCount()) + 1);
            courseMapper.updateById(c);
        }
        return Result.ok(c);
    }

    /** 社区帖子分页 */
    @GetMapping("/posts")
    public Result<PageResult<CommunityPost>> posts(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<CommunityPost> qw = new LambdaQueryWrapper<CommunityPost>()
                .eq(CommunityPost::getStatus, 1)
                .eq(topic != null && !topic.isEmpty(), CommunityPost::getTopic, topic)
                .and(keyword != null && !keyword.isEmpty(), w ->
                        w.like(CommunityPost::getTitle, keyword).or().like(CommunityPost::getContent, keyword))
                .orderByDesc(CommunityPost::getIsTop).orderByDesc(CommunityPost::getCreatedAt);
        Page<CommunityPost> p = postMapper.selectPage(new Page<>(page, size), qw);
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    /** 帖子详情 */
    @GetMapping("/posts/{id}")
    public Result<CommunityPost> postDetail(@PathVariable Long id) {
        CommunityPost post = postMapper.selectById(id);
        if (post != null) {
            post.setViewCount((post.getViewCount() == null ? 0 : post.getViewCount()) + 1);
            postMapper.updateById(post);
        }
        return Result.ok(post);
    }

    /** 活动列表 */
    @GetMapping("/activities")
    public Result<PageResult<TeaActivity>> activities(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<TeaActivity> qw = new LambdaQueryWrapper<TeaActivity>()
                .eq(TeaActivity::getStatus, 1)
                .eq(type != null && !type.isEmpty(), TeaActivity::getActivityType, type)
                .orderByDesc(TeaActivity::getStartTime);
        Page<TeaActivity> p = activityMapper.selectPage(new Page<>(page, size), qw);
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    /** 活动详情 */
    @GetMapping("/activities/{id}")
    public Result<TeaActivity> activityDetail(@PathVariable Long id) {
        return Result.ok(activityMapper.selectById(id));
    }
}
