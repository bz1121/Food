package com.tastefinder.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 餐厅收藏实体类
 * 
 * 对应数据库表: restaurant_favorites
 * 存储用户收藏的餐厅信息（POI ID + 快照数据）
 */
@Entity
@Table(name = "restaurant_favorites")
@Data
@EntityListeners(AuditingEntityListener.class)
public class RestaurantFavorite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "poi_id", nullable = false, length = 100)
    private String poiId;
    
    @Column(name = "restaurant_name", nullable = false, length = 200)
    private String restaurantName;
    
    @Column(nullable = false, length = 500)
    private String address;
    
    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;
    
    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;
    
    @Column(precision = 2, scale = 1)
    private BigDecimal rating;
    
    @Column(name = "cover_image", length = 500)
    private String coverImage;
    
    @Column(length = 100)
    private String category;
    
    @Column(name = "snapshot_data", columnDefinition = "JSON")
    private String snapshotData;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}

