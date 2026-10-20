package com.tea.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.common.PageResult;
import com.tea.common.Result;
import com.tea.entity.ActivityEnroll;
import com.tea.entity.TeaActivity;
import com.tea.mapper.ActivityEnrollMapper;
import com.tea.mapper.TeaActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 活动管理
 */
@RestController
@RequestMapping("/api/admin")
public class AdminActivityController {

    @Autowired private TeaActivityMapper activityMapper;
    @Autowired private ActivityEnrollMapper enrollMapper;

    @GetMapping("/activities")
    public Result<PageResult<TeaActivity>> activityList(
            @RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<TeaActivity> qw = new LambdaQueryWrapper<TeaActivity>()
                .like(keyword != null && !keyword.isEmpty(), TeaActivity::getTitle, keyword)
                .orderByDesc(TeaActivity::getCreatedAt);
        Page<TeaActivity> p = activityMapper.selectPage(new Page<>(page, size), qw);
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    @PostMapping("/activities")
    public Result<TeaActivity> addActivity(@RequestBody TeaActivity body) {
        body.setActivityId(null);
        body.setEnrolledCount(0);
        body.setCreatedAt(LocalDateTime.now());
        activityMapper.insert(body);
        return Result.ok(body);
    }

    @PutMapping("/activities/{id}")
    public Result<Void> updateActivity(@PathVariable Long id, @RequestBody TeaActivity body) {
        body.setActivityId(id);
        body.setUpdatedAt(LocalDateTime.now());
        activityMapper.updateById(body);
        return Result.ok();
    }

    @DeleteMapping("/activities/{id}")
    public Result<Void> deleteActivity(@PathVariable Long id) {
        activityMapper.deleteById(id);
        return Result.ok();
    }

    /** 活动报名名单 */
    @GetMapping("/activities/{id}/enrolls")
    public Result<PageResult<ActivityEnroll>> enrolls(@PathVariable Long id,
            @RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size) {
        Page<ActivityEnroll> p = enrollMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<ActivityEnroll>().eq(ActivityEnroll::getActivityId, id)
                        .orderByDesc(ActivityEnroll::getCreatedAt));
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    /** 全部报名列表 */
    @GetMapping("/enrolls")
    public Result<PageResult<ActivityEnroll>> allEnrolls(
            @RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size) {
        Page<ActivityEnroll> p = enrollMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<ActivityEnroll>().orderByDesc(ActivityEnroll::getCreatedAt));
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }
}
