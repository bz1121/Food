package com.tastefinder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tastefinder.dto.AddFavoriteRequest;
import com.tastefinder.dto.FavoriteDTO;
import com.tastefinder.dto.Restaurant;
import com.tastefinder.entity.RestaurantFavorite;
import com.tastefinder.repository.RestaurantFavoriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 餐厅收藏服务类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteService {
    
    private final RestaurantFavoriteRepository favoriteRepository;
    private final ObjectMapper objectMapper;
    
    /**
     * 添加收藏
     * 
     * @param userId 用户ID
     * @param request 收藏请求
     * @param restaurant 餐厅信息（从POI API获取）
     * @return 收藏信息
     */
    @Transactional
    public FavoriteDTO addFavorite(Long userId, AddFavoriteRequest request, Restaurant restaurant) {
        // 检查是否已收藏
        if (favoriteRepository.existsByUserIdAndPoiId(userId, request.getPoiId())) {
            throw new RuntimeException("您已收藏过该餐厅");
        }
        
        // 创建收藏记录
        RestaurantFavorite favorite = new RestaurantFavorite();
        favorite.setUserId(userId);
        favorite.setPoiId(request.getPoiId());
        favorite.setRestaurantName(restaurant.getName() != null ? restaurant.getName() : "未知餐厅");
        favorite.setAddress(restaurant.getAddress() != null ? restaurant.getAddress() : "");
        
        // 确保location不为null
        if (restaurant.getLocation() != null) {
            favorite.setLatitude(restaurant.getLocation().getLatitude());
            favorite.setLongitude(restaurant.getLocation().getLongitude());
        } else {
            favorite.setLatitude(java.math.BigDecimal.ZERO);
            favorite.setLongitude(java.math.BigDecimal.ZERO);
        }
        
        favorite.setRating(restaurant.getRating() != null ? restaurant.getRating() : java.math.BigDecimal.valueOf(4.0));
        favorite.setCoverImage(restaurant.getImages() != null && !restaurant.getImages().isEmpty() 
            ? restaurant.getImages().get(0) : null);
        favorite.setCategory(restaurant.getCategory());
        favorite.setNotes(request.getNotes());
        
        // 保存快照数据（JSON格式）
        try {
            String snapshotJson = objectMapper.writeValueAsString(restaurant);
            favorite.setSnapshotData(snapshotJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize restaurant snapshot", e);
        }
        
        RestaurantFavorite saved = favoriteRepository.save(favorite);
        
        log.info("User {} favorited restaurant: {}", userId, restaurant.getName());
        
        return convertToDTO(saved);
    }
    
    /**
     * 获取用户收藏列表
     */
    public Page<FavoriteDTO> getUserFavorites(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<RestaurantFavorite> favorites = favoriteRepository
            .findByUserIdOrderByCreatedAtDesc(userId, pageable);
        
        return favorites.map(this::convertToDTO);
    }
    
    /**
     * 取消收藏
     */
    @Transactional
    public void removeFavorite(Long userId, Long favoriteId) {
        RestaurantFavorite favorite = favoriteRepository.findById(favoriteId)
            .orElseThrow(() -> new RuntimeException("收藏不存在"));
        
        if (!favorite.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除他人的收藏");
        }
        
        favoriteRepository.delete(favorite);
        log.info("User {} removed favorite: {}", userId, favoriteId);
    }
    
    /**
     * 检查是否已收藏
     */
    public boolean isFavorited(Long userId, String poiId) {
        return favoriteRepository.existsByUserIdAndPoiId(userId, poiId);
    }
    
    /**
     * Entity转DTO
     */
    private FavoriteDTO convertToDTO(RestaurantFavorite favorite) {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setId(favorite.getId());
        dto.setPoiId(favorite.getPoiId());
        dto.setRestaurantName(favorite.getRestaurantName());
        dto.setAddress(favorite.getAddress());
        dto.setLatitude(favorite.getLatitude());
        dto.setLongitude(favorite.getLongitude());
        dto.setRating(favorite.getRating());
        dto.setCoverImage(favorite.getCoverImage());
        dto.setCategory(favorite.getCategory());
        dto.setNotes(favorite.getNotes());
        dto.setCreatedAt(favorite.getCreatedAt());
        
        return dto;
    }
}

