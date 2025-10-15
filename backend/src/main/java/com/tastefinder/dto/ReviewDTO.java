package com.tastefinder.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评价信息DTO
 */
@Data
public class ReviewDTO {
    
    private Long id;
    private ReviewUser user;
    private String poiId;
    private String restaurantName;
    private Integer rating;
    private String content;
    private List<String> images;
    private Integer helpfulCount;
    private LocalDateTime createdAt;
    
    @Data
    public static class ReviewUser {
        private Long id;
        private String username;
        private String nickname;
        private String avatarUrl;
        private Boolean isCertified;  // 是否认证评论家
    }
}

