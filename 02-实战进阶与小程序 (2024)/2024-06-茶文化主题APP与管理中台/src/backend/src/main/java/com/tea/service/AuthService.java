package com.tea.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.common.BizException;
import com.tea.entity.AppUser;
import com.tea.entity.SysAdmin;
import com.tea.entity.SysAdminRole;
import com.tea.entity.SysMenu;
import com.tea.entity.SysRole;
import com.tea.entity.SysRoleMenu;
import com.tea.mapper.AppUserMapper;
import com.tea.mapper.SysAdminMapper;
import com.tea.mapper.SysAdminRoleMapper;
import com.tea.mapper.SysMenuMapper;
import com.tea.mapper.SysRoleMapper;
import com.tea.mapper.SysRoleMenuMapper;
import com.tea.util.JwtUtil;
import com.tea.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 鉴权服务：App用户登录注册、管理员登录
 */
@Service
public class AuthService {

    @Autowired
    private AppUserMapper appUserMapper;
    @Autowired
    private SysAdminMapper sysAdminMapper;
    @Autowired
    private SysAdminRoleMapper sysAdminRoleMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * App用户登录
     */
    public Map<String, Object> appLogin(String username, String password) {
        AppUser user = appUserMapper.selectOne(
                new LambdaQueryWrapper<AppUser>().eq(AppUser::getUsername, username));
        if (user == null) {
            throw new BizException(400, "用户不存在");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BizException(403, "账号已被禁用");
        }
        if (!PasswordUtil.matches(password, user.getPassword())) {
            throw new BizException(400, "密码错误");
        }
        String token = jwtUtil.generateToken(user.getUserId(), "user");
        Map<String, Object> data = new HashMap<>();
        user.setPassword(null);
        data.put("token", token);
        data.put("user", user);
        return data;
    }

    /**
     * App用户注册
     */
    public Map<String, Object> appRegister(String username, String password, String nickname) {
        Long cnt = appUserMapper.selectCount(
                new LambdaQueryWrapper<AppUser>().eq(AppUser::getUsername, username));
        if (cnt != null && cnt > 0) {
            throw new BizException(400, "用户名已存在");
        }
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(PasswordUtil.encode(password));
        user.setNickname(nickname == null || nickname.isEmpty() ? username : nickname);
        user.setStatus(1);
        user.setPoints(0);
        user.setLevel(1);
        user.setCreatedAt(LocalDateTime.now());
        appUserMapper.insert(user);
        String token = jwtUtil.generateToken(user.getUserId(), "user");
        user.setPassword(null);
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);
        return data;
    }

    /**
     * 管理员登录
     */
    public Map<String, Object> adminLogin(String username, String password) {
        SysAdmin admin = sysAdminMapper.selectOne(
                new LambdaQueryWrapper<SysAdmin>().eq(SysAdmin::getUsername, username));
        if (admin == null) {
            throw new BizException(400, "管理员不存在");
        }
        if (admin.getStatus() != null && admin.getStatus() == 0) {
            throw new BizException(403, "账号已被禁用");
        }
        if (!PasswordUtil.matches(password, admin.getPassword())) {
            throw new BizException(400, "密码错误");
        }
        admin.setLastLogin(LocalDateTime.now());
        sysAdminMapper.updateById(admin);
        String token = jwtUtil.generateToken(admin.getAdminId(), "admin");
        admin.setPassword(null);
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("admin", admin);
        data.put("menus", getAdminMenus(admin.getAdminId()));
        return data;
    }

    /**
     * 获取管理员可访问菜单
     */
    public List<SysMenu> getAdminMenus(Long adminId) {
        List<SysAdminRole> ars = sysAdminRoleMapper.selectList(
                new LambdaQueryWrapper<SysAdminRole>().eq(SysAdminRole::getAdminId, adminId));
        if (ars.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> roleIds = ars.stream().map(SysAdminRole::getRoleId).collect(Collectors.toList());
        List<SysRoleMenu> rms = sysRoleMenuMapper.selectList(
                new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getRoleId, roleIds));
        if (rms.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> menuIds = rms.stream().map(SysRoleMenu::getMenuId).distinct().collect(Collectors.toList());
        return sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>()
                        .in(SysMenu::getMenuId, menuIds)
                        .orderByAsc(SysMenu::getSortOrder));
    }

    /**
     * 获取管理员角色
     */
    public List<SysRole> getAdminRoles(Long adminId) {
        List<SysAdminRole> ars = sysAdminRoleMapper.selectList(
                new LambdaQueryWrapper<SysAdminRole>().eq(SysAdminRole::getAdminId, adminId));
        if (ars.isEmpty()) return new ArrayList<>();
        List<Long> roleIds = ars.stream().map(SysAdminRole::getRoleId).collect(Collectors.toList());
        return sysRoleMapper.selectBatchIds(roleIds);
    }
}
