package com.tastefinder.repository;

import com.tastefinder.entity.RestaurantReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 餐厅评价Repository接口
 */
@Repository
public interface RestaurantReviewRepository extends JpaRepository<RestaurantReview, Long> {
    
    /**
     * 查询餐厅的所有评价（分页）
     */
    Page<RestaurantReview> findByPoiIdAndStatusOrderByCreatedAtDesc(
        String poiId, Integer status, Pageable pageable
    );
    
    /**
     * 查询用户的所有评价（分页）
     */
    Page<RestaurantReview> findByUserIdAndStatusOrderByCreatedAtDesc(
        Long userId, Integer status, Pageable pageable
    );
    
    /**
     * 统计餐厅评价数量
     */
    long countByPoiIdAndStatus(String poiId, Integer status);
    
    /**
     * 计算餐厅平均评分
     */
    @Query("SELECT AVG(r.rating) FROM RestaurantReview r WHERE r.poiId = ?1 AND r.status = 1")
    Double calculateAverageRating(String poiId);
    
    /**
     * 统计各星级评价数量
     */
    @Query("SELECT r.rating, COUNT(r) FROM RestaurantReview r " +
           "WHERE r.poiId = ?1 AND r.status = 1 GROUP BY r.rating")
    List<Object[]> getRatingDistribution(String poiId);
}

