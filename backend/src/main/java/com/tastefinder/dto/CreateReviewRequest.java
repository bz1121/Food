package com.tastefinder.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

/**
 * 创建评价请求DTO
 */
@Data
public class CreateReviewRequest {
    
    @NotBlank(message = "POI ID不能为空")
    private String poiId;
    
    @NotBlank(message = "餐厅名称不能为空")
    private String restaurantName;
    
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低1星")
    @Max(value = 5, message = "评分最高5星")
    private Integer rating;
    
    @NotBlank(message = "评价内容不能为空")
    @Size(min = 10, max = 2000, message = "评价内容长度必须在10-2000字之间")
    private String content;
    
    @Size(max = 9, message = "最多上传9张图片")
    private List<String> images;
}

