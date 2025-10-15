# TasteFinder Backend

TasteFinder美食推荐平台后端服务 - 基于Spring Boot 3.1.5

## 技术栈

- **Spring Boot** 3.1.5
- **Spring Security** + JWT认证
- **Spring Data JPA** + Hibernate
- **MySQL** 8.0
- **Redis** 7.x
- **Flyway** 数据库迁移
- **Springdoc OpenAPI** API文档

## 快速开始

### 1. 启动数据库

```bash
# 在项目根目录
docker-compose up -d mysql redis
```

### 2. 配置高德API密钥

编辑 `src/main/resources/application-dev.yml`:

```yaml
amap:
  key: your-amap-web-service-key
  secret: your-amap-secret
```

### 3. 运行应用

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

应用将在 http://localhost:8080 启动

### 4. 访问API文档

Swagger UI: http://localhost:8080/swagger-ui.html

## 测试账户

| 用户名 | 密码 | 角色 |
|--------|------|------|
| critic_a | password123 | 美食评论家A |
| critic_b | password123 | 西餐专家B |
| foodie_c | password123 | 探店达人C |
| user_d | password123 | 普通用户D |
| merchant_e | password123 | 商家代表E |

## 项目结构

```
backend/
├── src/main/java/com/tastefinder/
│   ├── TastefinderApplication.java  # 主应用类
│   ├── config/                      # 配置类
│   │   └── RedisCacheConfig.java
│   ├── controller/                  # 控制器
│   │   ├── AuthController.java
│   │   └── RestaurantController.java
│   ├── dto/                         # 数据传输对象
│   ├── entity/                      # JPA实体类
│   │   ├── User.java
│   │   ├── RestaurantFavorite.java
│   │   ├── RestaurantReview.java
│   │   └── ReviewImage.java
│   ├── repository/                  # 数据访问层
│   ├── security/                    # 安全配置
│   │   ├── JwtTokenProvider.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── SecurityConfig.java
│   ├── service/                     # 业务逻辑层
│   │   ├── UserService.java
│   │   ├── AuthService.java
│   │   └── AMapPOIService.java
│   └── exception/                   # 异常处理
│       ├── GlobalExceptionHandler.java
│       └── UsernameAlreadyExistsException.java
└── src/main/resources/
    ├── application.yml
    ├── application-dev.yml
    └── db/migration/                # Flyway迁移脚本
        ├── V1__init_schema.sql
        └── V2__init_test_data.sql
```

## API端点

### 认证相关
- POST /api/auth/register - 用户注册
- POST /api/auth/login - 用户登录

### 餐厅相关
- GET /api/restaurants/search - 搜索附近餐厅
- GET /api/restaurants/{poiId} - 获取餐厅详情

更多API详见 Swagger文档

## 开发指南

参见: [../specs/001-tastefinder-api/plan.md](../specs/001-tastefinder-api/plan.md)

