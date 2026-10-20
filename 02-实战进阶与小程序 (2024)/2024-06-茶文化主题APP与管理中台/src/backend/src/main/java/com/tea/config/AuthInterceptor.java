package com.tea.config;

import com.tea.common.Result;
import com.tea.util.JwtUtil;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录鉴权拦截器（JWT）
 * 放行：/api/auth/**、/api/public/**、静态资源
 * 管理端：/api/admin/** 需要 role=admin
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String uri = request.getRequestURI();
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (token == null || token.isEmpty()) {
            token = request.getParameter("token");
        }
        if (token == null || !jwtUtil.validate(token)) {
            writeAuthError(response, "未登录或登录已过期");
            return false;
        }
        Claims claims = jwtUtil.parseToken(token);
        request.setAttribute("userId", Long.valueOf(claims.getSubject()));
        request.setAttribute("role", claims.get("role"));
        // 管理端接口要求 admin 角色
        if (uri.startsWith("/api/admin/") && !"admin".equals(claims.get("role"))) {
            writeAuthError(response, "无管理员权限");
            return false;
        }
        return true;
    }

    private void writeAuthError(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(Result.fail(401, msg)));
    }
}
