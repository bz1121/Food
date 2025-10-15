package com.tastefinder;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Spring Boot应用上下文测试
 */
@SpringBootTest
@ActiveProfiles("test")
class TastefinderApplicationTests {
    
    @Test
    void contextLoads() {
        // 验证Spring上下文可以成功加载
    }
}

