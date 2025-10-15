package com.tastefinder.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收藏信息DTO
 */
@Data
public class FavoriteDTO {
    
    private Long id;
    private String poiId;
    private String restaurantName;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal rating;
    private String coverImage;
    private String category;
    private String notes;
    private LocalDateTime createdAt;
}

