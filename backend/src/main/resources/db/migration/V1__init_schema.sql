-- TasteFinder 数据库初始化脚本
-- 创建日期: 2025-10-14

-- 用户表
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  password VARCHAR(255) NOT NULL COMMENT '加密密码',
  nickname VARCHAR(100) COMMENT '昵称',
  avatar_url VARCHAR(500) COMMENT '头像URL',
  role_type VARCHAR(50) NOT NULL DEFAULT 'NORMAL_USER' COMMENT '用户角色',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '账户状态 1=正常 0=禁用',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  last_login_at TIMESTAMP NULL COMMENT '最后登录时间',
  INDEX idx_role_type (role_type),
  INDEX idx_created_at (created_at),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 餐厅收藏表
CREATE TABLE restaurant_favorites (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  poi_id VARCHAR(100) NOT NULL COMMENT '高德POI ID',
  restaurant_name VARCHAR(200) NOT NULL COMMENT '餐厅名称',
  address VARCHAR(500) NOT NULL COMMENT '餐厅地址',
  latitude DECIMAL(10,7) NOT NULL COMMENT '纬度',
  longitude DECIMAL(10,7) NOT NULL COMMENT '经度',
  rating DECIMAL(2,1) COMMENT '评分',
  cover_image VARCHAR(500) COMMENT '封面图片',
  category VARCHAR(100) COMMENT '餐厅类型',
  snapshot_data JSON COMMENT '完整快照数据',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  notes TEXT COMMENT '用户备注',
  UNIQUE KEY idx_user_poi (user_id, poi_id),
  INDEX idx_user_id (user_id),
  INDEX idx_created_at (created_at),
  INDEX idx_location (latitude, longitude),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='餐厅收藏表';

-- 餐厅评价表
CREATE TABLE restaurant_reviews (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评价ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  poi_id VARCHAR(100) NOT NULL COMMENT '高德POI ID',
  restaurant_name VARCHAR(200) NOT NULL COMMENT '餐厅名称',
  rating TINYINT NOT NULL COMMENT '星级评分 1-5',
  content TEXT NOT NULL COMMENT '评价内容',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态 1=已发布 0=已删除',
  is_certified_review BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否认证评论家评价',
  helpful_count INT NOT NULL DEFAULT 0 COMMENT '有用数',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted_at TIMESTAMP NULL COMMENT '删除时间',
  delete_reason VARCHAR(500) COMMENT '删除原因',
  INDEX idx_user_id (user_id),
  INDEX idx_poi_id (poi_id),
  INDEX idx_status_created (status, created_at),
  INDEX idx_rating (rating),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='餐厅评价表';

-- 评价图片表
CREATE TABLE review_images (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '图片ID',
  review_id BIGINT NOT NULL COMMENT '评价ID',
  image_url VARCHAR(500) NOT NULL COMMENT '图片URL',
  display_order TINYINT NOT NULL DEFAULT 0 COMMENT '显示顺序 0-8',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  INDEX idx_review_id (review_id),
  INDEX idx_review_order (review_id, display_order),
  FOREIGN KEY (review_id) REFERENCES restaurant_reviews(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评价图片表';

-- 浏览历史表
CREATE TABLE browse_history (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  poi_id VARCHAR(100) NOT NULL COMMENT '高德POI ID',
  restaurant_name VARCHAR(200) NOT NULL COMMENT '餐厅名称',
  visited_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  UNIQUE KEY idx_user_poi_visited (user_id, poi_id, visited_at),
  INDEX idx_user_visited (user_id, visited_at),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='浏览历史表';

