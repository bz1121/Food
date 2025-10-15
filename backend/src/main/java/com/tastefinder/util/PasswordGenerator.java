package com.tastefinder.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码生成工具
 * 
 * 用于生成BCrypt加密密码，可直接运行
 */
public class PasswordGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        
        System.out.println("========================================");
        System.out.println("BCrypt密码生成工具");
        System.out.println("========================================");
        System.out.println();
        
        // 生成测试账户密码
        String[] passwords = {
            "password123",  // 测试账户密码
            "FoodCritic@2024",
            "WesternFood#123",
            "Explorer888",
            "User123456",
            "Merchant2024"
        };
        
        String[] usernames = {
            "password123 (测试账户)",
            "FoodCritic@2024 (评论家A)",
            "WesternFood#123 (评论家B)",
            "Explorer888 (食客C)",
            "User123456 (用户D)",
            "Merchant2024 (商家E)"
        };
        
        for (int i = 0; i < passwords.length; i++) {
            String encrypted = encoder.encode(passwords[i]);
            System.out.println("原始密码: " + passwords[i]);
            System.out.println("用途: " + usernames[i]);
            System.out.println("加密后: " + encrypted);
            System.out.println();
        }
        
        System.out.println("========================================");
        System.out.println("将上面的加密密码复制到V2__init_test_data.sql");
        System.out.println("========================================");
    }
}

