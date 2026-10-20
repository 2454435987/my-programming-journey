package com.eldercare.controller;

import com.eldercare.common.ApiResponse;
import com.eldercare.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 登录认证接口
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** 微信授权登录(演示: code 可传 demo_elder / demo_volunteer / demo_admin) */
    @PostMapping("/login")
    public ApiResponse login(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(authService.login(
                (String) body.get("code"),
                body.get("role") == null ? null : String.valueOf(body.get("role"))));
    }

    /** 老人身份绑定(提交管理员审核) */
    @PostMapping("/bind-elder")
    public ApiResponse bindElder(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(authService.bindElder(
                ((Number) b.get("userId")).longValue(),
                str(b, "elderName"), str(b, "gender"), str(b, "phone"), str(b, "address"),
                str(b, "community"), str(b, "healthNote"), str(b, "emergencyContact"),
                str(b, "emergencyPhone"),
                b.get("lng") == null ? null : ((Number) b.get("lng")).doubleValue(),
                b.get("lat") == null ? null : ((Number) b.get("lat")).doubleValue()));
    }

    /** 更新联系方式 */
    @PostMapping("/update-contact")
    public ApiResponse updateContact(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(authService.updateContact(
                ((Number) b.get("userId")).longValue(),
                str(b, "phone"), str(b, "emergencyContact"), str(b, "emergencyPhone")));
    }

    @GetMapping("/profile")
    public ApiResponse profile(@RequestParam long userId) {
        Map<String, Object> u = new java.util.HashMap<>();
        u.put("id", userId);
        Map<String, Object> user = new java.util.HashMap<>();
        user.put("id", userId);
        return ApiResponse.ok(authService.loadProfile(user));
    }

    private static String str(Map<String, Object> b, String k) {
        Object v = b.get(k);
        return v == null ? "" : String.valueOf(v);
    }
}
