package com.tea.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.common.BizException;
import com.tea.common.PageResult;
import com.tea.common.Result;
import com.tea.entity.TeaCategory;
import com.tea.entity.TeaCourse;
import com.tea.entity.TeaKnowledge;
import com.tea.mapper.TeaCategoryMapper;
import com.tea.mapper.TeaCourseMapper;
import com.tea.mapper.TeaKnowledgeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 内容管理：知识、教程、分类
 */
@RestController
@RequestMapping("/api/admin")
public class AdminContentController {

    @Autowired private TeaKnowledgeMapper knowledgeMapper;
    @Autowired private TeaCourseMapper courseMapper;
    @Autowired private TeaCategoryMapper categoryMapper;

    // ========== 知识 ==========
    @GetMapping("/knowledge")
    public Result<PageResult<TeaKnowledge>> knowledgeList(
            @RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String keyword, @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<TeaKnowledge> qw = new LambdaQueryWrapper<TeaKnowledge>()
                .like(keyword != null && !keyword.isEmpty(), TeaKnowledge::getTitle, keyword)
                .eq(status != null, TeaKnowledge::getStatus, status)
                .orderByDesc(TeaKnowledge::getCreatedAt);
        Page<TeaKnowledge> p = knowledgeMapper.selectPage(new Page<>(page, size), qw);
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    @PostMapping("/knowledge")
    public Result<TeaKnowledge> addKnowledge(@RequestBody TeaKnowledge body) {
        body.setKnowledgeId(null);
        body.setViewCount(0); body.setLikeCount(0);
        body.setCreatedAt(LocalDateTime.now());
        knowledgeMapper.insert(body);
        return Result.ok(body);
    }

    @PutMapping("/knowledge/{id}")
    public Result<Void> updateKnowledge(@PathVariable Long id, @RequestBody TeaKnowledge body) {
        body.setKnowledgeId(id);
        body.setUpdatedAt(LocalDateTime.now());
        knowledgeMapper.updateById(body);
        return Result.ok();
    }

    @DeleteMapping("/knowledge/{id}")
    public Result<Void> deleteKnowledge(@PathVariable Long id) {
        knowledgeMapper.deleteById(id);
        return Result.ok();
    }

    // ========== 教程 ==========
    @GetMapping("/courses")
    public Result<PageResult<TeaCourse>> courseList(
            @RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<TeaCourse> qw = new LambdaQueryWrapper<TeaCourse>()
                .like(keyword != null && !keyword.isEmpty(), TeaCourse::getTitle, keyword)
                .orderByDesc(TeaCourse::getCreatedAt);
        Page<TeaCourse> p = courseMapper.selectPage(new Page<>(page, size), qw);
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    @PostMapping("/courses")
    public Result<TeaCourse> addCourse(@RequestBody TeaCourse body) {
        body.setCourseId(null);
        body.setViewCount(0); body.setLikeCount(0);
        body.setCreatedAt(LocalDateTime.now());
        courseMapper.insert(body);
        return Result.ok(body);
    }

    @PutMapping("/courses/{id}")
    public Result<Void> updateCourse(@PathVariable Long id, @RequestBody TeaCourse body) {
        body.setCourseId(id);
        body.setUpdatedAt(LocalDateTime.now());
        courseMapper.updateById(body);
        return Result.ok();
    }

    @DeleteMapping("/courses/{id}")
    public Result<Void> deleteCourse(@PathVariable Long id) {
        courseMapper.deleteById(id);
        return Result.ok();
    }

    // ========== 分类 ==========
    @GetMapping("/categories")
    public Result<java.util.List<TeaCategory>> categories() {
        return Result.ok(categoryMapper.selectList(new LambdaQueryWrapper<TeaCategory>()
                .orderByAsc(TeaCategory::getSortOrder)));
    }

    @PostMapping("/categories")
    public Result<TeaCategory> addCategory(@RequestBody TeaCategory body) {
        body.setCategoryId(null);
        body.setCreatedAt(LocalDateTime.now());
        categoryMapper.insert(body);
        return Result.ok(body);
    }

    @PutMapping("/categories/{id}")
    public Result<Void> updateCategory(@PathVariable Long id, @RequestBody TeaCategory body) {
        body.setCategoryId(id);
        categoryMapper.updateById(body);
        return Result.ok();
    }

    @DeleteMapping("/categories/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryMapper.deleteById(id);
        return Result.ok();
    }
}
