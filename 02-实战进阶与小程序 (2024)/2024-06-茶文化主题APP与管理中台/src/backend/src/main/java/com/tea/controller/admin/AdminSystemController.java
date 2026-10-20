package com.tea.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.common.BizException;
import com.tea.common.PageResult;
import com.tea.common.Result;
import com.tea.entity.*;
import com.tea.mapper.*;
import com.tea.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 系统管理：管理员、角色、菜单、日志
 */
@RestController
@RequestMapping("/api/admin")
public class AdminSystemController {

    @Autowired private SysAdminMapper adminMapper;
    @Autowired private SysRoleMapper roleMapper;
    @Autowired private SysMenuMapper menuMapper;
    @Autowired private SysAdminRoleMapper adminRoleMapper;
    @Autowired private SysRoleMenuMapper roleMenuMapper;
    @Autowired private SysOperationLogMapper logMapper;

    /** 管理员列表 */
    @GetMapping("/admins")
    public Result<PageResult<SysAdmin>> admins(@RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        Page<SysAdmin> p = adminMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<SysAdmin>().orderByDesc(SysAdmin::getCreatedAt));
        p.getRecords().forEach(a -> a.setPassword(null));
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    /** 新增管理员 */
    @PostMapping("/admins")
    public Result<SysAdmin> addAdmin(@RequestBody SysAdmin body) {
        Long cnt = adminMapper.selectCount(new LambdaQueryWrapper<SysAdmin>()
                .eq(SysAdmin::getUsername, body.getUsername()));
        if (cnt != null && cnt > 0) throw new BizException(400, "用户名已存在");
        body.setPassword(PasswordUtil.encode(body.getPassword() == null ? "admin123" : body.getPassword()));
        body.setCreatedAt(LocalDateTime.now());
        adminMapper.insert(body);
        body.setPassword(null);
        return Result.ok(body);
    }

    /** 修改管理员 */
    @PutMapping("/admins/{id}")
    public Result<Void> updateAdmin(@PathVariable Long id, @RequestBody SysAdmin body) {
        SysAdmin old = adminMapper.selectById(id);
        if (old == null) throw new BizException(404, "管理员不存在");
        body.setAdminId(id);
        if (body.getPassword() == null || body.getPassword().isEmpty()) {
            body.setPassword(old.getPassword());
        } else {
            body.setPassword(PasswordUtil.encode(body.getPassword()));
        }
        adminMapper.updateById(body);
        return Result.ok();
    }

    /** 角色列表 */
    @GetMapping("/roles")
    public Result<List<SysRole>> roles() {
        return Result.ok(roleMapper.selectList(new LambdaQueryWrapper<SysRole>()
                .orderByAsc(SysRole::getRoleId)));
    }

    /** 新增角色 */
    @PostMapping("/roles")
    public Result<SysRole> addRole(@RequestBody SysRole body) {
        body.setCreatedAt(LocalDateTime.now());
        roleMapper.insert(body);
        return Result.ok(body);
    }

    /** 菜单树 */
    @GetMapping("/menus")
    public Result<List<SysMenu>> menus() {
        return Result.ok(menuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                .orderByAsc(SysMenu::getSortOrder)));
    }

    /** 操作日志 */
    @GetMapping("/logs")
    public Result<PageResult<SysOperationLog>> logs(@RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<SysOperationLog> qw = new LambdaQueryWrapper<SysOperationLog>()
                .like(keyword != null && !keyword.isEmpty(), SysOperationLog::getOperation, keyword)
                .orderByDesc(SysOperationLog::getCreatedAt);
        Page<SysOperationLog> p = logMapper.selectPage(new Page<>(page, size), qw);
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    /** 写入操作日志 */
    @PostMapping("/logs")
    public Result<Void> addLog(@RequestAttribute("userId") Long userId, @RequestBody SysOperationLog body) {
        body.setLogId(null);
        body.setAdminId(userId);
        body.setCreatedAt(LocalDateTime.now());
        logMapper.insert(body);
        return Result.ok();
    }
}
