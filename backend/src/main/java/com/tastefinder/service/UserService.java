package com.tastefinder.service;

import com.tastefinder.dto.RegisterRequest;
import com.tastefinder.dto.UserDTO;
import com.tastefinder.entity.RoleType;
import com.tastefinder.entity.User;
import com.tastefinder.exception.UsernameAlreadyExistsException;
import com.tastefinder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务类
 * 
 * 处理用户注册、用户信息管理等业务逻辑
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 用户注册
     * 
     * @param request 注册请求
     * @return 用户信息DTO
     * @throws UsernameAlreadyExistsException 用户名已存在
     */
    @Transactional
    public UserDTO register(RegisterRequest request) {
        // 1. 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Registration failed: username already exists - {}", request.getUsername());
            throw new UsernameAlreadyExistsException(request.getUsername());
        }
        
        // 2. 创建用户对象
        User user = new User();
        user.setUsername(request.getUsername());
        
        // 3. 加密密码
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encryptedPassword);
        
        // 4. 设置昵称（如果提供）
        user.setNickname(request.getNickname() != null ? 
                        request.getNickname() : request.getUsername());
        
        // 5. 设置默认角色
        user.setRoleType(RoleType.NORMAL_USER);
        
        // 6. 保存到数据库
        User savedUser = userRepository.save(user);
        
        log.info("User registered successfully: {}", savedUser.getUsername());
        
        // 7. 转换为DTO返回（不包含密码）
        return convertToDTO(savedUser);
    }
    
    /**
     * 根据用户名查找用户
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }
    
    /**
     * 根据ID查找用户
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElse(null);
    }
    
    /**
     * Entity转DTO
     */
    public UserDTO convertToDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setNickname(user.getNickname());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setRoleType(user.getRoleType());
        dto.setStatus(user.getStatus());  // 添加状态字段
        dto.setCreatedAt(user.getCreatedAt());
        dto.setLastLoginAt(user.getLastLoginAt());
        
        return dto;
    }
}

