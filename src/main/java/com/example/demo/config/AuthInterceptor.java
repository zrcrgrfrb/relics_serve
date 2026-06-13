package com.example.demo.config;

import com.example.demo.entity.AdminUser;
import com.example.demo.repository.AdminUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ADMIN_ROLE = "administrator";
    private static final Set<String> PUBLIC_GET_PREFIXES = Set.of(
            "/api/categories",
            "/api/relics"
    );

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod().toUpperCase();
        String uri = request.getRequestURI();
        if ("OPTIONS".equals(method) || ("GET".equals(method) && isPublicGet(uri))) {
            return true;
        }

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            writeUnauthorized(response, "未认证，请先登录");
            return false;
        }

        String token = authHeader.substring(BEARER_PREFIX.length()).trim();
        if (!jwtUtil.validateToken(token) || !ADMIN_ROLE.equals(jwtUtil.getRoleFromToken(token))) {
            writeUnauthorized(response, "Token 无效或已过期");
            return false;
        }

        String username = jwtUtil.getUsernameFromToken(token);
        AdminUser admin = adminUserRepository.findByUsername(username).orElse(null);
        if (admin == null || !ADMIN_ROLE.equals(admin.getRole())) {
            writeUnauthorized(response, "管理员账号不可用");
            return false;
        }

        request.setAttribute("username", username);
        request.setAttribute("role", admin.getRole());
        return true;
    }

    private boolean isPublicGet(String uri) {
        return PUBLIC_GET_PREFIXES.stream().anyMatch(prefix -> uri.equals(prefix) || uri.startsWith(prefix + "/"));
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"success\":false,\"data\":null,\"message\":\"" + message + "\"}");
    }
}
