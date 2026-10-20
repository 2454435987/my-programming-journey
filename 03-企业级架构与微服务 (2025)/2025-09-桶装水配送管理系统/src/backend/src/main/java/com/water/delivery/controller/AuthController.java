package com.water.delivery.controller;

import com.water.delivery.common.Result;
import com.water.delivery.dto.LoginDTO;
import com.water.delivery.dto.PasswordDTO;
import com.water.delivery.security.JwtTokenProvider;
import com.water.delivery.service.UserService;
import com.water.delivery.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Tag(name = "登录与认证模块")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    @Operation(summary = "用户统一登录鉴权 (PC端管理员 / 手机端送水工)")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success("登录成功", loginVO);
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/info")
    public Result<LoginVO> getInfo(HttpServletRequest request) {
        String token = resolveToken(request);
        if (!StringUtils.hasText(token) || !tokenProvider.validateToken(token)) {
            return Result.error(401, "凭证无效或已过期");
        }
        Long userId = tokenProvider.getUserIdFromToken(token);
        LoginVO info = userService.getUserInfo(userId);
        return Result.success(info);
    }

    @Operation(summary = "用户修改登录密码")
    @PutMapping("/password")
    public Result<Boolean> changePassword(HttpServletRequest request, @Valid @RequestBody PasswordDTO passwordDTO) {
        String token = resolveToken(request);
        Long userId = tokenProvider.getUserIdFromToken(token);
        boolean success = userService.changePassword(userId, passwordDTO);
        return Result.success("密码修改成功，请妥善保管新密码", success);
    }

    @Operation(summary = "送水工申请重置密码工单")
    @PostMapping("/pwd-reset-req")
    public Result<String> applyPasswordReset(@RequestParam String username,
                                             @RequestParam String phone,
                                             @RequestParam(required = false) String reason) {
        // 演示环境直接返回提交成功提示
        return Result.success("重置申请已提交至管理员审核，审核通过后将重置为初始密码 123456", "APPLY_OK");
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }
}
