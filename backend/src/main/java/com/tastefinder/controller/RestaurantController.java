package com.tastefinder.controller;

import com.tastefinder.dto.Restaurant;
import com.tastefinder.service.AMapPOIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 餐厅控制器
 * 
 * 处理餐厅搜索、详情查询等请求
 */
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurant", description = "餐厅相关接口")
public class RestaurantController {
    
    private final AMapPOIService amapPOIService;
    private final com.tastefinder.service.FavoriteService favoriteService;
    private final com.tastefinder.repository.RestaurantReviewRepository reviewRepository;
    
    /**
     * 搜索附近餐厅
     */
    @GetMapping("/search")
    @Operation(summary = "搜索附近餐厅", description = "基于用户位置搜索附近餐厅（调用高德POI API）")
    public ResponseEntity<Map<String, Object>> search(
            @Parameter(description = "纬度") @RequestParam double latitude,
            @Parameter(description = "经度") @RequestParam double longitude,
            @Parameter(description = "搜索半径（米）") @RequestParam(defaultValue = "5000") int radius,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "排序方式") @RequestParam(defaultValue = "distance") String sortBy
    ) {
        // 搜索餐厅
        List<Restaurant> restaurants = amapPOIService.searchNearby(latitude, longitude, radius);
        
        // TODO: 根据sortBy排序（distance, rating, price）
        // TODO: 根据keyword过滤
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", restaurants);
        response.put("total", restaurants.size());
        response.put("pagination", Map.of(
            "page", 1,
            "size", restaurants.size(),
            "total", restaurants.size()
        ));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取餐厅详情
     */
    @GetMapping("/{poiId}")
    @Operation(summary = "获取餐厅详情", description = "根据高德POI ID获取餐厅详细信息")
    public ResponseEntity<Restaurant> getRestaurantDetail(
            @Parameter(description = "高德POI ID") @PathVariable String poiId,
            Authentication authentication
    ) {
        Long userId = authentication != null ? (Long) authentication.getPrincipal() : null;
        
        // TODO: 从高德API获取基础信息，这里使用示例数据
        Restaurant restaurant = new Restaurant();
        restaurant.setPoiId(poiId);
        restaurant.setName("示例餐厅");
        
        // 从数据库获取用户评价计算的评分
        restaurant.setRating(getRestaurantRatingFromReviews(poiId));
        restaurant.setReviewCount(getRestaurantReviewCount(poiId));
        
        // 检查是否已收藏
        if (userId != null) {
            restaurant.setIsFavorited(favoriteService.isFavorited(userId, poiId));
        }
        
        return ResponseEntity.ok(restaurant);
    }
    
    private java.math.BigDecimal getRestaurantRatingFromReviews(String poiId) {
        // 从ReviewRepository计算平均分
        Double avgRating = reviewRepository.calculateAverageRating(poiId);
        if (avgRating != null && avgRating > 0) {
            return java.math.BigDecimal.valueOf(avgRating).setScale(1, java.math.RoundingMode.HALF_UP);
        }
        // 如果没有评价，返回null或默认值
        return null;
    }
    
    private Long getRestaurantReviewCount(String poiId) {
        return reviewRepository.countByPoiIdAndStatus(poiId, 1);
    }
}

