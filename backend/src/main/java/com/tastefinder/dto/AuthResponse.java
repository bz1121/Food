package com.tastefinder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 认证响应DTO
 * 
 * 包含JWT token和用户信息
 */
@Data
@AllArgsConstructor
public class AuthResponse {
    
    /**
     * JWT access token
     */
    private String token;
    
    /**
     * 用户信息
     */
    private UserDTO user;
}

