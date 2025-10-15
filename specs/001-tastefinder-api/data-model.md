# 数据模型设计

**项目**: TasteFinder 美食推荐平台  
**数据库**: MySQL 8.0  
**ORM**: Spring Data JPA  
**创建日期**: 2025-10-14

---

## 数据库 Schema 概览

```
tastefinder
├── users (用户表)
├── user_roles (用户角色关联表)
├── restaurant_favorites (餐厅收藏表)
├── restaurant_reviews (餐厅评价表)
├── review_images (评价图片表)
└── browse_history (浏览历史表 - 可选)
```

---

## 1. 用户表 (users)

### 用途
存储用户基本信息和认证凭证

### 字段定义

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 用户ID |
| username | VARCHAR(50) | UNIQUE, NOT NULL | 用户名（登录用） |
| password | VARCHAR(255) | NOT NULL | 加密密码（BCrypt） |
| nickname | VARCHAR(100) | NULL | 昵称（显示用） |
| avatar_url | VARCHAR(500) | NULL | 头像URL |
| role_type | VARCHAR(50) | NOT NULL, DEFAULT 'NORMAL_USER' | 用户角色类型 |
| status | TINYINT | NOT NULL, DEFAULT 1 | 账户状态（1=正常 0=禁用） |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |
| last_login_at | TIMESTAMP | NULL | 最后登录时间 |

### 索引
- PRIMARY KEY (`id`)
- UNIQUE KEY `idx_username` (`username`)
- INDEX `idx_role_type` (`role_type`)
- INDEX `idx_created_at` (`created_at`)

### 角色类型枚举
```sql
role_type VALUES:
- 'NORMAL_USER'           -- 普通用户
- 'FOOD_CRITIC_A'         -- 美食评论家A（专业认证）
- 'FOOD_CRITIC_B'         -- 美食评论家B（西餐专家）
- 'SENIOR_FOODIE'         -- 资深食客（探店达人）
- 'MERCHANT'              -- 商家代表
```

### 预设测试账户
```sql
INSERT INTO users (username, password, nickname, role_type) VALUES
('critic_a', '$2a$10$...', '专业评论家A', 'FOOD_CRITIC_A'),
('critic_b', '$2a$10$...', '西餐专家B', 'FOOD_CRITIC_B'),
('foodie_c', '$2a$10$...', '探店达人C', 'SENIOR_FOODIE'),
('user_d', '$2a$10$...', '普通用户D', 'NORMAL_USER'),
('merchant_e', '$2a$10$...', '商家代表E', 'MERCHANT');
```

### Entity Class (JPA)
```java
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(length = 100)
    private String nickname;
    
    @Column(length = 500)
    private String avatarUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RoleType roleType = RoleType.NORMAL_USER;
    
    @Column(nullable = false)
    private Integer status = 1;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    private LocalDateTime lastLoginAt;
}
```

---

## 2. 餐厅收藏表 (restaurant_favorites)

### 用途
存储用户收藏的餐厅信息（包含高德POI ID和快照数据）

### 字段定义

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 收藏ID |
| user_id | BIGINT | NOT NULL, FOREIGN KEY | 用户ID |
| poi_id | VARCHAR(100) | NOT NULL | 高德POI ID |
| restaurant_name | VARCHAR(200) | NOT NULL | 餐厅名称（快照） |
| address | VARCHAR(500) | NOT NULL | 餐厅地址（快照） |
| latitude | DECIMAL(10,7) | NOT NULL | 纬度（快照） |
| longitude | DECIMAL(10,7) | NOT NULL | 经度（快照） |
| rating | DECIMAL(2,1) | NULL | 评分（快照，0.0-5.0） |
| cover_image | VARCHAR(500) | NULL | 封面图片URL（快照） |
| category | VARCHAR(100) | NULL | 餐厅类型（快照） |
| snapshot_data | JSON | NULL | 完整快照（JSON格式） |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 收藏时间 |
| notes | TEXT | NULL | 用户备注 |

### 索引
- PRIMARY KEY (`id`)
- UNIQUE KEY `idx_user_poi` (`user_id`, `poi_id`)
- INDEX `idx_user_id` (`user_id`)
- INDEX `idx_created_at` (`created_at`)
- INDEX `idx_location` (`latitude`, `longitude`)

### 快照数据结构 (snapshot_data JSON)
```json
{
  "poi_id": "B001B0I32K",
  "name": "海底捞火锅",
  "address": "北京市朝阳区建国路88号",
  "location": {
    "lat": 39.912345,
    "lon": 116.454321
  },
  "rating": 4.5,
  "price_range": "$$",
  "phone": "010-12345678",
  "opening_hours": "10:00-22:00",
  "images": ["url1", "url2"],
  "cached_at": "2025-10-14T10:30:00Z"
}
```

### Entity Class
```java
@Entity
@Table(name = "restaurant_favorites")
@Data
public class RestaurantFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false, length = 100)
    private String poiId;
    
    @Column(nullable = false, length = 200)
    private String restaurantName;
    
    @Column(nullable = false, length = 500)
    private String address;
    
    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;
    
    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;
    
    @Column(precision = 2, scale = 1)
    private BigDecimal rating;
    
    @Column(length = 500)
    private String coverImage;
    
    @Column(length = 100)
    private String category;
    
    @Column(columnDefinition = "JSON")
    private String snapshotData;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
```

---

## 3. 餐厅评价表 (restaurant_reviews)

### 用途
存储用户对餐厅的评价（星级、文字、图片）

### 字段定义

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 评价ID |
| user_id | BIGINT | NOT NULL, FOREIGN KEY | 用户ID |
| poi_id | VARCHAR(100) | NOT NULL | 高德POI ID |
| restaurant_name | VARCHAR(200) | NOT NULL | 餐厅名称 |
| rating | TINYINT | NOT NULL | 星级评分（1-5） |
| content | TEXT | NOT NULL | 评价内容（200-2000字） |
| status | TINYINT | NOT NULL, DEFAULT 1 | 审核状态（1=已发布 0=已删除 2=待审核） |
| is_certified_review | BOOLEAN | NOT NULL, DEFAULT FALSE | 是否认证评论家评价 |
| helpful_count | INT | NOT NULL, DEFAULT 0 | 有用数 |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 发布时间 |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |
| deleted_at | TIMESTAMP | NULL | 删除时间 |
| delete_reason | VARCHAR(500) | NULL | 删除原因 |

### 索引
- PRIMARY KEY (`id`)
- INDEX `idx_user_id` (`user_id`)
- INDEX `idx_poi_id` (`poi_id`)
- INDEX `idx_status_created` (`status`, `created_at`)
- INDEX `idx_rating` (`rating`)

### 业务规则
- `content`长度验证：200-2000字符
- `rating`范围验证：1-5
- `is_certified_review`: 根据user的role_type自动设置（FOOD_CRITIC_*角色为true）
- 评论家评价在列表中排序靠前

### Entity Class
```java
@Entity
@Table(name = "restaurant_reviews")
@Data
public class RestaurantReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false, length = 100)
    private String poiId;
    
    @Column(nullable = false, length = 200)
    private String restaurantName;
    
    @Column(nullable = false)
    @Min(1) @Max(5)
    private Integer rating;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    @Size(min = 200, max = 2000)
    private String content;
    
    @Column(nullable = false)
    private Integer status = 1;
    
    @Column(nullable = false)
    private Boolean isCertifiedReview = false;
    
    @Column(nullable = false)
    private Integer helpfulCount = 0;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private LocalDateTime deletedAt;
    
    @Column(length = 500)
    private String deleteReason;
    
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImage> images;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
```

---

## 4. 评价图片表 (review_images)

### 用途
存储评价关联的图片（一个评价最多9张）

### 字段定义

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 图片ID |
| review_id | BIGINT | NOT NULL, FOREIGN KEY | 评价ID |
| image_url | VARCHAR(500) | NOT NULL | 图片URL |
| display_order | TINYINT | NOT NULL, DEFAULT 0 | 显示顺序（0-8） |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 上传时间 |

### 索引
- PRIMARY KEY (`id`)
- INDEX `idx_review_id` (`review_id`)
- INDEX `idx_review_order` (`review_id`, `display_order`)

### 业务规则
- 每个review最多9张图片
- `display_order`范围：0-8
- 删除review时级联删除关联图片

### Entity Class
```java
@Entity
@Table(name = "review_images")
@Data
public class ReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long reviewId;
    
    @Column(nullable = false, length = 500)
    private String imageUrl;
    
    @Column(nullable = false)
    @Min(0) @Max(8)
    private Integer displayOrder = 0;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", insertable = false, updatable = false)
    private RestaurantReview review;
}
```

---

## 5. 浏览历史表 (browse_history) - 可选

### 用途
记录用户最近浏览的餐厅（最多保留30天）

### 字段定义

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 记录ID |
| user_id | BIGINT | NOT NULL, FOREIGN KEY | 用户ID |
| poi_id | VARCHAR(100) | NOT NULL | 高德POI ID |
| restaurant_name | VARCHAR(200) | NOT NULL | 餐厅名称 |
| visited_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 浏览时间 |

### 索引
- PRIMARY KEY (`id`)
- UNIQUE KEY `idx_user_poi_visited` (`user_id`, `poi_id`, `visited_at`)
- INDEX `idx_user_visited` (`user_id`, `visited_at`)

### 数据保留策略
- 自动删除30天前的记录（定时任务或TTL）
- 每个用户最多保留100条记录

---

## 数据库初始化脚本

### schema.sql
```sql
CREATE DATABASE IF NOT EXISTS tastefinder 
  DEFAULT CHARACTER SET utf8mb4 
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE tastefinder;

-- 用户表
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  nickname VARCHAR(100),
  avatar_url VARCHAR(500),
  role_type VARCHAR(50) NOT NULL DEFAULT 'NORMAL_USER',
  status TINYINT NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  last_login_at TIMESTAMP NULL,
  INDEX idx_role_type (role_type),
  INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 餐厅收藏表
CREATE TABLE restaurant_favorites (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  poi_id VARCHAR(100) NOT NULL,
  restaurant_name VARCHAR(200) NOT NULL,
  address VARCHAR(500) NOT NULL,
  latitude DECIMAL(10,7) NOT NULL,
  longitude DECIMAL(10,7) NOT NULL,
  rating DECIMAL(2,1),
  cover_image VARCHAR(500),
  category VARCHAR(100),
  snapshot_data JSON,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  notes TEXT,
  UNIQUE KEY idx_user_poi (user_id, poi_id),
  INDEX idx_user_id (user_id),
  INDEX idx_created_at (created_at),
  INDEX idx_location (latitude, longitude),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 餐厅评价表
CREATE TABLE restaurant_reviews (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  poi_id VARCHAR(100) NOT NULL,
  restaurant_name VARCHAR(200) NOT NULL,
  rating TINYINT NOT NULL,
  content TEXT NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  is_certified_review BOOLEAN NOT NULL DEFAULT FALSE,
  helpful_count INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP NULL,
  delete_reason VARCHAR(500),
  INDEX idx_user_id (user_id),
  INDEX idx_poi_id (poi_id),
  INDEX idx_status_created (status, created_at),
  INDEX idx_rating (rating),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 评价图片表
CREATE TABLE review_images (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  review_id BIGINT NOT NULL,
  image_url VARCHAR(500) NOT NULL,
  display_order TINYINT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_review_id (review_id),
  INDEX idx_review_order (review_id, display_order),
  FOREIGN KEY (review_id) REFERENCES restaurant_reviews(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 浏览历史表（可选）
CREATE TABLE browse_history (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  poi_id VARCHAR(100) NOT NULL,
  restaurant_name VARCHAR(200) NOT NULL,
  visited_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY idx_user_poi_visited (user_id, poi_id, visited_at),
  INDEX idx_user_visited (user_id, visited_at),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### data.sql (预设测试数据)
```sql
-- 插入测试用户（密码均为: password123）
INSERT INTO users (username, password, nickname, role_type) VALUES
('critic_a', '$2a$10$N9qo8uLOickgx2ZMRZoMye3FZL/VdCmKyZChKVdL7bNJqIJ9H5PYe', '专业评论家A', 'FOOD_CRITIC_A'),
('critic_b', '$2a$10$N9qo8uLOickgx2ZMRZoMye3FZL/VdCmKyZChKVdL7bNJqIJ9H5PYe', '西餐专家B', 'FOOD_CRITIC_B'),
('foodie_c', '$2a$10$N9qo8uLOickgx2ZMRZoMye3FZL/VdCmKyZChKVdL7bNJqIJ9H5PYe', '探店达人C', 'SENIOR_FOODIE'),
('user_d', '$2a$10$N9qo8uLOickgx2ZMRZoMye3FZL/VdCmKyZChKVdL7bNJqIJ9H5PYe', '普通用户D', 'NORMAL_USER'),
('merchant_e', '$2a$10$N9qo8uLOickgx2ZMRZoMye3FZL/VdCmKyZChKVdL7bNJqIJ9H5PYe', '商家代表E', 'MERCHANT');
```

---

## 实体关系图 (ER Diagram)

```
┌─────────────┐
│    users    │
│ (用户表)     │
└──────┬──────┘
       │
       │ 1:N
       │
   ┌───┴───────────────────┬─────────────────┐
   │                       │                 │
   ▼                       ▼                 ▼
┌─────────────────┐ ┌─────────────────┐ ┌─────────────┐
│restaurant_favorites│ │restaurant_reviews│ │browse_history│
│ (收藏表)         │ │ (评价表)         │ │ (浏览历史)   │
└──────────────────┘ └────────┬────────┘ └─────────────┘
                              │
                              │ 1:N
                              │
                              ▼
                    ┌──────────────────┐
                    │  review_images   │
                    │  (评价图片表)     │
                    └──────────────────┘
```

---

## 数据迁移策略

### 版本管理
使用 **Flyway** 或 **Liquibase** 管理数据库版本

### 迁移脚本命名
```
V1__init_schema.sql
V2__add_browse_history.sql
V3__add_rating_index.sql
```

### 回滚策略
- 保留每个版本的回滚脚本
- 生产环境变更前在测试环境验证
- 关键表变更需要数据备份

---

## 性能优化建议

1. **索引优化**
   - 高频查询字段添加索引（user_id, poi_id）
   - 复合索引优化范围查询（status + created_at）
   - 定期分析慢查询日志

2. **分区策略**（数据量大时）
   - 按时间分区`restaurant_reviews`表（按月）
   - 按用户ID范围分区`restaurant_favorites`

3. **缓存策略**
   - 热门餐厅评价缓存Redis（TTL 30分钟）
   - 用户收藏列表缓存（用户操作后失效）

4. **数据归档**
   - 浏览历史自动删除30天前数据
   - 已删除评价归档到历史表

---

## 数据安全

1. **敏感数据加密**
   - 密码使用BCrypt加密（strength=10）
   - 生产环境数据库连接使用SSL

2. **备份策略**
   - 每日全量备份
   - 每小时增量备份（binlog）
   - 备份数据异地存储

3. **审计日志**
   - 记录敏感操作（删除评价、禁用用户）
   - 日志保留90天

---

## 数据模型总结

TasteFinder数据模型设计遵循以下原则：

1. **规范化**: 减少数据冗余，保证一致性
2. **性能优化**: 合理索引，支持高频查询
3. **可扩展性**: 预留JSON字段支持灵活扩展
4. **数据完整性**: 外键约束+应用层验证
5. **审计友好**: 时间戳字段记录完整生命周期

该模型支持100万用户、1000万评价的规模，满足项目初期和中期需求。

