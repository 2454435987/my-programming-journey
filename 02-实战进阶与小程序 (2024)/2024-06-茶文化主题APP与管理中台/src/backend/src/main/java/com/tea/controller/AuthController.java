package com.tea.controller;

import com.tea.common.Result;
import com.tea.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 鉴权接口：登录/注册
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /** App用户登录 */
    @PostMapping("/app/login")
    public Result<Map<String, Object>> appLogin(@RequestBody Map<String, String> body) {
        return Result.ok(authService.appLogin(body.get("username"), body.get("password")));
    }

    /** App用户注册 */
    @PostMapping("/app/register")
    public Result<Map<String, Object>> appRegister(@RequestBody Map<String, String> body) {
        return Result.ok(authService.appRegister(body.get("username"), body.get("password"), body.get("nickname")));
    }

    /** 管理员登录 */
    @PostMapping("/admin/login")
    public Result<Map<String, Object>> adminLogin(@RequestBody Map<String, String> body) {
        return Result.ok(authService.adminLogin(body.get("username"), body.get("password")));
    }

    /** 获取当前管理员信息 */
    @GetMapping("/admin/info")
    public Result<Map<String, Object>> adminInfo(@RequestAttribute("userId") Long userId) {
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("menus", authService.getAdminMenus(userId));
        data.put("roles", authService.getAdminRoles(userId));
        return Result.ok(data);
    }
}
