package com.tastefinder.repository;

import com.tastefinder.entity.RestaurantFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 餐厅收藏Repository接口
 */
@Repository
public interface RestaurantFavoriteRepository extends JpaRepository<RestaurantFavorite, Long> {
    
    /**
     * 查询用户的所有收藏（分页）
     */
    Page<RestaurantFavorite> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    /**
     * 查找用户对特定餐厅的收藏
     */
    Optional<RestaurantFavorite> findByUserIdAndPoiId(Long userId, String poiId);
    
    /**
     * 检查用户是否已收藏某餐厅
     */
    boolean existsByUserIdAndPoiId(Long userId, String poiId);
    
    /**
     * 统计用户收藏数量
     */
    long countByUserId(Long userId);
}

