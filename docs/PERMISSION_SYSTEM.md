# 🔐 权限系统说明

**更新时间**: 2025-10-14  
**状态**: ✅ 已配置

---

## 👥 用户角色

### 6个角色类型

| 角色 | RoleType | 权限 | 测试账户 |
|------|----------|------|---------|
| **管理员** | ADMIN | 所有权限 | admin / password123 |
| 美食评论家A | FOOD_CRITIC_A | 认证评价 | critic_a / password123 |
| 西餐专家B | FOOD_CRITIC_B | 认证评价 | critic_b / password123 |
| 探店达人 | SENIOR_FOODIE | 普通权限 | foodie_c / password123 |
| 普通用户 | NORMAL_USER | 普通权限 | user_d / password123 |
| 商家代表 | MERCHANT | 查看统计 | merchant_e / password123 |

---

## 🔒 权限控制

### 前端路由守卫

**权限检查**:
```javascript
// 检查是否需要管理员权限
if (to.meta.requiresAdmin) {
  if (user.roleType !== 'ADMIN') {
    alert('访问被拒绝：需要管理员权限')
    next('/')  // 跳转回首页
  }
}
```

**受保护的路由**:
- `/admin/**` - 需要ADMIN角色

### 前端UI控制

**管理后台入口**:
```vue
<!-- 只有管理员能看到管理后台菜单 -->
<el-dropdown-item v-if="user?.roleType === 'ADMIN'">
  🛠️ 管理后台
</el-dropdown-item>
```

**非管理员**: 菜单中不显示"管理后台"选项

---

## 🎯 权限分配

### ADMIN（管理员）

**可访问**:
- ✅ 所有普通用户功能
- ✅ 管理后台（/admin）
- ✅ 用户管理
- ✅ 评价审核
- ✅ 数据统计

**特殊权限**:
- 删除任何用户的评价
- 禁用/启用用户账户
- 查看所有数据统计

### 评论家（FOOD_CRITIC_A/B）

**特权**:
- ✅ 评价带认证徽章
- ✅ 评价权重更高
- ✅ isCertifiedReview = true

### 普通用户（NORMAL_USER）

**基础功能**:
- ✅ 浏览地图
- ✅ 搜索餐厅
- ✅ 收藏餐厅
- ✅ 发表评价
- ✅ 路径导航

---

## 🔑 测试账户

### 管理员账户

**用户名**: `admin`  
**密码**: `password123`  
**角色**: ADMIN

**可以做**:
- 登录后台管理
- 审核评价
- 管理用户

### 普通用户账户

**用户名**: `user_d`  
**密码**: `password123`  
**角色**: NORMAL_USER

**限制**:
- 看不到"管理后台"菜单
- 无法访问/admin路由

---

## 🚀 测试权限

### 测试1: 管理员访问后台

1. 使用 `admin` / `password123` 登录
2. 点击右上角用户菜单
3. **应该看到"🛠️ 管理后台"选项** ✅
4. 点击进入管理界面

### 测试2: 普通用户无法访问

1. 使用 `user_d` / `password123` 登录
2. 点击右上角用户菜单
3. **不应该看到"管理后台"选项** ✅
4. 手动访问 http://localhost:5173/admin/dashboard
5. **应该被拦截并提示"需要管理员权限"** ✅

---

## 🔧 需要重置数据库

**因为添加了admin用户，需要重置数据库**:

```bash
docker-compose down -v
docker-compose up -d
# 等待30秒
# 重启后端
```

---

**权限系统已配置！重置数据库后测试！** 🔐✅
