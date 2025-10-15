package com.tastefinder.controller;

import com.tastefinder.dto.AddFavoriteRequest;
import com.tastefinder.dto.FavoriteDTO;
import com.tastefinder.dto.Restaurant;
import com.tastefinder.service.AMapPOIService;
import com.tastefinder.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 收藏控制器
 * 
 * 处理餐厅收藏相关请求
 */
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorite", description = "餐厅收藏相关接口")
@SecurityRequirement(name = "BearerAuth")
public class FavoriteController {
    
    private final FavoriteService favoriteService;
    private final AMapPOIService amapPOIService;
    
    /**
     * 获取收藏列表
     */
    @GetMapping
    @Operation(summary = "获取收藏列表", description = "获取当前用户的收藏列表")
    public ResponseEntity<Map<String, Object>> getFavorites(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Long userId = (Long) authentication.getPrincipal();
        
        Page<FavoriteDTO> favorites = favoriteService.getUserFavorites(userId, page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", favorites.getContent());
        response.put("pagination", Map.of(
            "page", page,
            "size", size,
            "total", favorites.getTotalElements(),
            "totalPages", favorites.getTotalPages()
        ));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 添加收藏
     */
    @PostMapping
    @Operation(summary = "添加收藏", description = "收藏餐厅")
    public ResponseEntity<FavoriteDTO> addFavorite(
            Authentication authentication,
            @Valid @RequestBody AddFavoriteRequest request
    ) {
        Long userId = (Long) authentication.getPrincipal();
        
        // 使用前端传来的餐厅信息
        Restaurant restaurant = new Restaurant();
        restaurant.setPoiId(request.getPoiId());
        restaurant.setName(request.getRestaurantName());
        restaurant.setAddress(request.getAddress());
        
        Restaurant.Location location = new Restaurant.Location();
        location.setLatitude(java.math.BigDecimal.valueOf(request.getLatitude()));
        location.setLongitude(java.math.BigDecimal.valueOf(request.getLongitude()));
        restaurant.setLocation(location);
        
        if (request.getRating() != null) {
            restaurant.setRating(java.math.BigDecimal.valueOf(request.getRating()));
        }
        restaurant.setCategory(request.getCategory());
        
        FavoriteDTO favorite = favoriteService.addFavorite(userId, request, restaurant);
        
        return new ResponseEntity<>(favorite, HttpStatus.CREATED);
    }
    
    /**
     * 取消收藏
     */
    @DeleteMapping("/{favoriteId}")
    @Operation(summary = "取消收藏", description = "删除收藏的餐厅")
    public ResponseEntity<Void> removeFavorite(
            Authentication authentication,
            @PathVariable Long favoriteId
    ) {
        Long userId = (Long) authentication.getPrincipal();
        
        favoriteService.removeFavorite(userId, favoriteId);
        
        return ResponseEntity.noContent().build();
    }
}

