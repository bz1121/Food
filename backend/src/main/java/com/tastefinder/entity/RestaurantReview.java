package com.tastefinder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 餐厅评价实体类
 * 
 * 对应数据库表: restaurant_reviews
 */
@Entity
@Table(name = "restaurant_reviews")
@Data
@EntityListeners(AuditingEntityListener.class)
public class RestaurantReview {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "poi_id", nullable = false, length = 100)
    private String poiId;
    
    @Column(name = "restaurant_name", nullable = false, length = 200)
    private String restaurantName;
    
    @Column(nullable = false, columnDefinition = "TINYINT")
    @Min(1)
    @Max(5)
    private Integer rating;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    @Size(min = 10, max = 2000)
    private String content;
    
    @Column(nullable = false)
    private Integer status = 1;
    
    @Column(name = "is_certified_review", nullable = false)
    private Boolean isCertifiedReview = false;
    
    @Column(name = "helpful_count", nullable = false)
    private Integer helpfulCount = 0;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    @Column(name = "delete_reason", length = 500)
    private String deleteReason;
    
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> images = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}

