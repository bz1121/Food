package com.tastefinder.security;

import com.tastefinder.entity.RoleType;
import com.tastefinder.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JwtTokenProvider 单元测试
 */
@DisplayName("JWT Token Provider测试")
class JwtTokenProviderTest {
    
    private JwtTokenProvider jwtTokenProvider;
    private User testUser;
    
    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        
        // 使用反射设置私有字段
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", 
            "test-secret-key-for-testing-only-minimum-256-bits-required");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpiration", 3600000L);
        
        // 创建测试用户
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setRoleType(RoleType.NORMAL_USER);
    }
    
    @Test
    @DisplayName("应该成功生成JWT token")
    void testGenerateToken_Success() {
        // When
        String token = jwtTokenProvider.generateToken(testUser);
        
        // Then
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(token.split("\\.").length == 3); // JWT格式: header.payload.signature
    }
    
    @Test
    @DisplayName("应该能从token中提取用户ID")
    void testGetUserIdFromToken() {
        // Given
        String token = jwtTokenProvider.generateToken(testUser);
        
        // When
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        
        // Then
        assertEquals(1L, userId);
    }
    
    @Test
    @DisplayName("应该能从token中提取用户名")
    void testGetUsernameFromToken() {
        // Given
        String token = jwtTokenProvider.generateToken(testUser);
        
        // When
        String username = jwtTokenProvider.getUsernameFromToken(token);
        
        // Then
        assertEquals("testuser", username);
    }
    
    @Test
    @DisplayName("有效的token应该验证通过")
    void testValidateToken_ValidToken() {
        // Given
        String token = jwtTokenProvider.generateToken(testUser);
        
        // When
        boolean isValid = jwtTokenProvider.validateToken(token);
        
        // Then
        assertTrue(isValid);
    }
    
    @Test
    @DisplayName("无效的token应该验证失败")
    void testValidateToken_InvalidToken() {
        // When
        boolean isValid = jwtTokenProvider.validateToken("invalid.token.here");
        
        // Then
        assertFalse(isValid);
    }
    
    @Test
    @DisplayName("空token应该验证失败")
    void testValidateToken_EmptyToken() {
        // When
        boolean isValid = jwtTokenProvider.validateToken("");
        
        // Then
        assertFalse(isValid);
    }
}

