package com.tastefinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * TasteFinder 美食推荐平台主应用
 * 
 * @author TasteFinder Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
public class TastefinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(TastefinderApplication.class, args);
    }
}

