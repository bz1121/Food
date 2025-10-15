-- 插入5个预设测试用户
-- 所有密码均为: password123
-- BCrypt加密后的hash (使用PasswordGenerator.java生成)

INSERT INTO users (username, password, nickname, role_type) VALUES
('admin', '$2a$10$bqtxDhxQ5.ETf5clXX6aOuw5/alNq9mL0QZdnPiAyIS1iusBw48f2', '系统管理员', 'ADMIN'),
('critic_a', '$2a$10$bqtxDhxQ5.ETf5clXX6aOuw5/alNq9mL0QZdnPiAyIS1iusBw48f2', '专业评论家A', 'FOOD_CRITIC_A'),
('critic_b', '$2a$10$bqtxDhxQ5.ETf5clXX6aOuw5/alNq9mL0QZdnPiAyIS1iusBw48f2', '西餐专家B', 'FOOD_CRITIC_B'),
('foodie_c', '$2a$10$bqtxDhxQ5.ETf5clXX6aOuw5/alNq9mL0QZdnPiAyIS1iusBw48f2', '探店达人C', 'SENIOR_FOODIE'),
('user_d', '$2a$10$bqtxDhxQ5.ETf5clXX6aOuw5/alNq9mL0QZdnPiAyIS1iusBw48f2', '普通用户D', 'NORMAL_USER'),
('merchant_e', '$2a$10$bqtxDhxQ5.ETf5clXX6aOuw5/alNq9mL0QZdnPiAyIS1iusBw48f2', '商家代表E', 'MERCHANT');

