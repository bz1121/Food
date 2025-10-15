package com.tastefinder.service;

import com.tastefinder.dto.AuthResponse;
import com.tastefinder.dto.UserDTO;
import com.tastefinder.entity.User;
import com.tastefinder.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 认证服务类
 * 
 * 处理登录、token生成等认证相关业务逻辑
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 密码
     * @return 认证响应（包含token和用户信息）
     * @throws BadCredentialsException 用户名或密码错误
     */
    @Transactional
    public AuthResponse login(String username, String password) {
        // 1. 根据用户名查找用户
        User user = userService.findByUsername(username);
        
        if (user == null) {
            log.warn("Login failed: user not found - {}", username);
            throw new BadCredentialsException("用户名或密码错误");
        }
        
        // 2. 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Login failed: invalid password - {}", username);
            throw new BadCredentialsException("用户名或密码错误");
        }
        
        // 3. 检查账户状态
        if (user.getStatus() != 1) {
            log.warn("Login failed: account disabled - {}", username);
            throw new BadCredentialsException("账户已被禁用");
        }
        
        // 4. 生成JWT token
        String token = jwtTokenProvider.generateToken(user);
        
        // 5. 更新最后登录时间
        user.setLastLoginAt(LocalDateTime.now());
        
        log.info("User logged in successfully: {}", username);
        
        // 6. 返回认证响应
        UserDTO userDTO = userService.convertToDTO(user);
        return new AuthResponse(token, userDTO);
    }
}

