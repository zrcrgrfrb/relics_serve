package com.example.demo.controller;

import com.example.demo.config.JwtUtil;
import com.example.demo.dto.AdminLoginRequest;
import com.example.demo.dto.AdminLoginResult;
import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.AdminUser;
import com.example.demo.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminLoginResult>> login(@RequestBody AdminLoginRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            return ResponseEntity.ok(ApiResponse.error("账号或密码错误"));
        }

        AdminUser admin = adminUserRepository.findByUsername(request.getUsername()).orElse(null);
        if (admin == null) {
            return ResponseEntity.ok(ApiResponse.error("账号或密码错误"));
        }

        // 简单密码校验（生产环境建议使用 BCrypt）
        if (!admin.getPassword().equals(request.getPassword())) {
            return ResponseEntity.ok(ApiResponse.error("账号或密码错误"));
        }

        String token = jwtUtil.generateToken(admin.getUsername(), admin.getRole());
        AdminLoginResult result = new AdminLoginResult(token, admin.getUsername(), admin.getRole());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
