package com.tea.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.common.BizException;
import com.tea.common.PageResult;
import com.tea.common.Result;
import com.tea.entity.AppUser;
import com.tea.mapper.AppUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Autowired private AppUserMapper userMapper;

    /** 用户分页 */
    @GetMapping
    public Result<PageResult<AppUser>> list(@RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<AppUser> qw = new LambdaQueryWrapper<AppUser>()
                .and(keyword != null && !keyword.isEmpty(), w ->
                        w.like(AppUser::getUsername, keyword).or().like(AppUser::getNickname, keyword))
                .eq(status != null, AppUser::getStatus, status)
                .orderByDesc(AppUser::getCreatedAt);
        Page<AppUser> p = userMapper.selectPage(new Page<>(page, size), qw);
        p.getRecords().forEach(u -> u.setPassword(null));
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    /** 修改用户状态 */
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody AppUser body) {
        AppUser u = userMapper.selectById(id);
        if (u == null) throw new BizException(404, "用户不存在");
        u.setStatus(body.getStatus());
        u.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(u);
        return Result.ok();
    }

    /** 用户详情 */
    @GetMapping("/{id}")
    public Result<AppUser> detail(@PathVariable Long id) {
        AppUser u = userMapper.selectById(id);
        if (u != null) u.setPassword(null);
        return Result.ok(u);
    }
}
