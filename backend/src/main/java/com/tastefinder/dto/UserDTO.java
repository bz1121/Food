package com.tastefinder.dto;

import com.tastefinder.entity.RoleType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息DTO（不包含密码）
 */
@Data
public class UserDTO {
    
    private Long id;
    private String username;
    private String nickname;
    private String avatarUrl;
    private RoleType roleType;
    private Integer status;  // 账户状态 1=正常 0=禁用
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}

