package com.tastefinder.entity;

/**
 * 用户角色类型枚举
 * 
 * 预设5类用户角色，对应不同的权限和功能
 */
public enum RoleType {
    /**
     * 普通用户 - 默认角色
     */
    NORMAL_USER,
    
    /**
     * 美食评论家A - 专业认证评论家
     */
    FOOD_CRITIC_A,
    
    /**
     * 美食评论家B - 西餐专家
     */
    FOOD_CRITIC_B,
    
    /**
     * 资深食客 - 探店达人
     */
    SENIOR_FOODIE,
    
    /**
     * 商家代表 - 可查看餐厅统计数据
     */
    MERCHANT,
    
    /**
     * 管理员 - 最高权限
     */
    ADMIN;
    
    /**
     * 判断是否为认证评论家角色
     */
    public boolean isCritic() {
        return this == FOOD_CRITIC_A || this == FOOD_CRITIC_B;
    }
    
    /**
     * 判断是否为管理员角色
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }
}

