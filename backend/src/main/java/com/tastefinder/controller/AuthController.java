package com.tastefinder.controller;

import com.tastefinder.dto.AuthResponse;
import com.tastefinder.dto.LoginRequest;
import com.tastefinder.dto.RegisterRequest;
import com.tastefinder.dto.UserDTO;
import com.tastefinder.security.JwtTokenProvider;
import com.tastefinder.service.AuthService;
import com.tastefinder.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 
 * 处理用户注册、登录等认证相关请求
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "用户认证相关接口")
public class AuthController {
    
    private final UserService userService;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "创建新用户账户（无需邮箱验证）")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "注册成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "409", description = "用户名已存在")
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        // 1. 注册用户
        UserDTO user = userService.register(request);
        
        // 2. 生成token（注册后自动登录）
        String token = jwtTokenProvider.generateToken(
            userService.findById(user.getId())
        );
        
        // 3. 返回认证响应
        AuthResponse response = new AuthResponse(token, user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用用户名和密码登录，返回JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "登录成功"),
        @ApiResponse(responseCode = "401", description = "用户名或密码错误")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}

