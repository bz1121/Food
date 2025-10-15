package com.tastefinder.service;

import com.tastefinder.dto.RegisterRequest;
import com.tastefinder.dto.UserDTO;
import com.tastefinder.entity.RoleType;
import com.tastefinder.entity.User;
import com.tastefinder.exception.UsernameAlreadyExistsException;
import com.tastefinder.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * UserService单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("用户服务测试")
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UserService userService;
    
    private RegisterRequest validRequest;
    
    @BeforeEach
    void setUp() {
        validRequest = new RegisterRequest();
        validRequest.setUsername("testuser");
        validRequest.setPassword("Test1234");
        validRequest.setNickname("测试用户");
    }
    
    @Test
    @DisplayName("注册新用户 - 成功")
    void testRegister_Success() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setNickname("测试用户");
        savedUser.setRoleType(RoleType.NORMAL_USER);
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        // When
        UserDTO result = userService.register(validRequest);
        
        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("测试用户", result.getNickname());
        assertEquals(RoleType.NORMAL_USER, result.getRoleType());
        
        verify(userRepository).existsByUsername("testuser");
        verify(passwordEncoder).encode("Test1234");
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    @DisplayName("注册新用户 - 用户名已存在")
    void testRegister_UsernameExists() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(true);
        
        // When & Then
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            userService.register(validRequest);
        });
        
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    @DisplayName("密码应该被加密存储")
    void testRegister_PasswordEncrypted() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode("Test1234")).thenReturn("$2a$10$encrypted");
        
        User savedUser = new User();
        savedUser.setPassword("$2a$10$encrypted");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        // When
        userService.register(validRequest);
        
        // Then
        verify(passwordEncoder).encode("Test1234");
    }
    
    @Test
    @DisplayName("新用户默认角色应该是NORMAL_USER")
    void testRegister_DefaultRole() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encrypted");
        
        User savedUser = new User();
        savedUser.setRoleType(RoleType.NORMAL_USER);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        // When
        UserDTO result = userService.register(validRequest);
        
        // Then
        assertEquals(RoleType.NORMAL_USER, result.getRoleType());
    }
}

