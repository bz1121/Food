package com.tastefinder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tastefinder.dto.LoginRequest;
import com.tastefinder.dto.RegisterRequest;
import com.tastefinder.entity.RoleType;
import com.tastefinder.entity.User;
import com.tastefinder.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AuthController 集成测试
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("认证API集成测试")
class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }
    
    @Test
    @DisplayName("注册新用户 - 成功返回201和token")
    void testRegister_Success() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("NewUser123");
        request.setNickname("新用户");
        
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.username").value("newuser"))
                .andExpect(jsonPath("$.user.roleType").value("NORMAL_USER"));
    }
    
    @Test
    @DisplayName("注册 - 用户名已存在返回409")
    void testRegister_UsernameExists() throws Exception {
        // 先创建用户
        User existingUser = new User();
        existingUser.setUsername("existing");
        existingUser.setPassword(passwordEncoder.encode("password"));
        existingUser.setRoleType(RoleType.NORMAL_USER);
        userRepository.save(existingUser);
        
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existing");
        request.setPassword("Password123");
        
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("USERNAME_EXISTS"));
    }
    
    @Test
    @DisplayName("登录 - 成功返回token")
    void testLogin_Success() throws Exception {
        // 先创建用户
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("Test1234"));
        user.setRoleType(RoleType.NORMAL_USER);
        userRepository.save(user);
        
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("Test1234");
        
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.username").value("testuser"));
    }
    
    @Test
    @DisplayName("登录 - 密码错误返回401")
    void testLogin_WrongPassword() throws Exception {
        // 先创建用户
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("Test1234"));
        user.setRoleType(RoleType.NORMAL_USER);
        userRepository.save(user);
        
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("WrongPassword");
        
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("BAD_CREDENTIALS"));
    }
}

