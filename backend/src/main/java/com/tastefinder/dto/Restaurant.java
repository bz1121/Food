package com.tastefinder.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 餐厅信息DTO
 */
@Data
public class Restaurant {
    
    /**
     * 高德POI ID
     */
    private String poiId;
    
    /**
     * 餐厅名称
     */
    private String name;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * 位置信息
     */
    private Location location;
    
    /**
     * 距离当前位置（米）
     */
    private Double distance;
    
    /**
     * 评分（0-5）
     */
    private BigDecimal rating;
    
    /**
     * 餐厅类型
     */
    private String category;
    
    /**
     * 价格区间
     */
    private String priceRange;
    
    /**
     * 电话
     */
    private String phone;
    
    /**
     * 图片列表
     */
    private List<String> images;
    
    /**
     * 营业时间
     */
    private String openingHours;
    
    /**
     * 人均消费
     */
    private Integer avgPrice;
    
    /**
     * 评价数量
     */
    private Long reviewCount;
    
    /**
     * 当前用户是否已收藏
     */
    private Boolean isFavorited;
    
    @Data
    public static class Location {
        private BigDecimal latitude;
        private BigDecimal longitude;
    }
}

