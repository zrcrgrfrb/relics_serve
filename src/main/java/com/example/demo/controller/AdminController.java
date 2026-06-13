package com.example.demo.controller;

import com.example.demo.config.JwtUtil;
import com.example.demo.dto.AdminLoginRequest;
import com.example.demo.dto.AdminLoginResult;
import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.AdminUser;
import com.example.demo.repository.AdminUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(10);
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(12);
    private static final String ADMIN_ROLE = "administrator";
    private static final Map<String, LoginAttempt> LOGIN_ATTEMPTS = new ConcurrentHashMap<>();

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminLoginResult>> login(@RequestBody AdminLoginRequest request,
                                                               HttpServletRequest servletRequest) {
        String username = request.getUsername() == null ? "" : request.getUsername().trim();
        String password = request.getPassword() == null ? "" : request.getPassword();
        String attemptKey = clientIp(servletRequest) + ":" + username.toLowerCase(Locale.ROOT);

        if (username.isBlank() || password.isBlank()) {
            registerFailure(attemptKey);
            return ResponseEntity.ok(ApiResponse.error("账号或密码错误"));
        }

        if (isLocked(attemptKey)) {
            return ResponseEntity.ok(ApiResponse.error("登录失败次数过多，请稍后再试"));
        }

        AdminUser admin = adminUserRepository.findByUsername(username).orElse(null);
        if (admin == null || !ADMIN_ROLE.equals(admin.getRole())) {
            registerFailure(attemptKey);
            return ResponseEntity.ok(ApiResponse.error("账号或密码错误"));
        }

        if (!passwordMatches(admin, password)) {
            registerFailure(attemptKey);
            return ResponseEntity.ok(ApiResponse.error("账号或密码错误"));
        }

        LOGIN_ATTEMPTS.remove(attemptKey);
        String token = jwtUtil.generateToken(admin.getUsername(), admin.getRole());
        AdminLoginResult result = new AdminLoginResult(token, admin.getUsername(), admin.getRole());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    private boolean passwordMatches(AdminUser admin, String rawPassword) {
        String storedPassword = admin.getPassword();
        if (storedPassword == null || storedPassword.isBlank()) {
            return false;
        }

        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
            return PASSWORD_ENCODER.matches(rawPassword, storedPassword);
        }

        boolean legacyPlaintextMatch = storedPassword.equals(rawPassword);
        if (legacyPlaintextMatch) {
            admin.setPassword(PASSWORD_ENCODER.encode(rawPassword));
            adminUserRepository.save(admin);
        }
        return legacyPlaintextMatch;
    }

    private boolean isLocked(String attemptKey) {
        LoginAttempt attempt = LOGIN_ATTEMPTS.get(attemptKey);
        if (attempt == null || attempt.failedCount < MAX_FAILED_ATTEMPTS) {
            return false;
        }
        if (Instant.now().isAfter(attempt.lastFailedAt.plus(LOCK_DURATION))) {
            LOGIN_ATTEMPTS.remove(attemptKey);
            return false;
        }
        return true;
    }

    private void registerFailure(String attemptKey) {
        LOGIN_ATTEMPTS.compute(attemptKey, (key, attempt) -> {
            if (attempt == null || Instant.now().isAfter(attempt.lastFailedAt.plus(LOCK_DURATION))) {
                return new LoginAttempt(1, Instant.now());
            }
            return new LoginAttempt(attempt.failedCount + 1, Instant.now());
        });
    }

    private String clientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private record LoginAttempt(int failedCount, Instant lastFailedAt) {
    }
}
