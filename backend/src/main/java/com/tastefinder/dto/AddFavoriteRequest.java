package com.tastefinder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 添加收藏请求DTO
 */
@Data
public class AddFavoriteRequest {
    
    @NotBlank(message = "POI ID不能为空")
    private String poiId;
    
    private String notes;
    
    // 从前端传来的餐厅信息
    private String restaurantName;
    private String address;
    private Double latitude;
    private Double longitude;
    private Double rating;
    private String coverImage;
    private String category;
}

