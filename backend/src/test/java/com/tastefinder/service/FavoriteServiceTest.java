package com.tastefinder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tastefinder.dto.AddFavoriteRequest;
import com.tastefinder.dto.FavoriteDTO;
import com.tastefinder.dto.Restaurant;
import com.tastefinder.entity.RestaurantFavorite;
import com.tastefinder.repository.RestaurantFavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * FavoriteService 单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("收藏服务测试")
class FavoriteServiceTest {
    
    @Mock
    private RestaurantFavoriteRepository favoriteRepository;
    
    @Mock
    private ObjectMapper objectMapper;
    
    @InjectMocks
    private FavoriteService favoriteService;
    
    private AddFavoriteRequest request;
    private Restaurant restaurant;
    
    @BeforeEach
    void setUp() {
        request = new AddFavoriteRequest();
        request.setPoiId("B001");
        request.setNotes("很好吃");
        
        restaurant = new Restaurant();
        restaurant.setPoiId("B001");
        restaurant.setName("测试餐厅");
        restaurant.setAddress("测试地址");
        Restaurant.Location location = new Restaurant.Location();
        location.setLatitude(BigDecimal.valueOf(39.908823));
        location.setLongitude(BigDecimal.valueOf(116.397470));
        restaurant.setLocation(location);
        restaurant.setRating(BigDecimal.valueOf(4.5));
    }
    
    @Test
    @DisplayName("添加收藏 - 成功")
    void testAddFavorite_Success() throws Exception {
        // Given
        when(favoriteRepository.existsByUserIdAndPoiId(anyLong(), anyString())).thenReturn(false);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");
        
        RestaurantFavorite saved = new RestaurantFavorite();
        saved.setId(1L);
        saved.setUserId(1L);
        saved.setPoiId("B001");
        saved.setRestaurantName("测试餐厅");
        
        when(favoriteRepository.save(any(RestaurantFavorite.class))).thenReturn(saved);
        
        // When
        FavoriteDTO result = favoriteService.addFavorite(1L, request, restaurant);
        
        // Then
        assertNotNull(result);
        assertEquals("B001", result.getPoiId());
        assertEquals("测试餐厅", result.getRestaurantName());
        
        verify(favoriteRepository).existsByUserIdAndPoiId(1L, "B001");
        verify(favoriteRepository).save(any(RestaurantFavorite.class));
    }
    
    @Test
    @DisplayName("添加收藏 - 重复收藏应抛出异常")
    void testAddFavorite_Duplicate() {
        // Given
        when(favoriteRepository.existsByUserIdAndPoiId(1L, "B001")).thenReturn(true);
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            favoriteService.addFavorite(1L, request, restaurant);
        });
        
        verify(favoriteRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("取消收藏 - 成功")
    void testRemoveFavorite_Success() {
        // Given
        RestaurantFavorite favorite = new RestaurantFavorite();
        favorite.setId(1L);
        favorite.setUserId(1L);
        
        when(favoriteRepository.findById(1L)).thenReturn(java.util.Optional.of(favorite));
        
        // When
        favoriteService.removeFavorite(1L, 1L);
        
        // Then
        verify(favoriteRepository).delete(favorite);
    }
    
    @Test
    @DisplayName("取消收藏 - 无权删除他人收藏应抛出异常")
    void testRemoveFavorite_Unauthorized() {
        // Given
        RestaurantFavorite favorite = new RestaurantFavorite();
        favorite.setId(1L);
        favorite.setUserId(2L);  // 不同的用户
        
        when(favoriteRepository.findById(1L)).thenReturn(java.util.Optional.of(favorite));
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            favoriteService.removeFavorite(1L, 1L);
        });
        
        verify(favoriteRepository, never()).delete(any());
    }
}

