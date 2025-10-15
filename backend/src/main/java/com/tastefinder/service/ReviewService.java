package com.tastefinder.service;

import com.tastefinder.dto.CreateReviewRequest;
import com.tastefinder.dto.ReviewDTO;
import com.tastefinder.entity.RestaurantReview;
import com.tastefinder.entity.RoleType;
import com.tastefinder.entity.User;
import com.tastefinder.repository.RestaurantReviewRepository;
import com.tastefinder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * 餐厅评价服务类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    
    private final RestaurantReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ContentFilterService contentFilterService;
    
    /**
     * 创建评价（先发后审）
     */
    @Transactional
    public ReviewDTO createReview(Long userId, CreateReviewRequest request) {
        // 1. 关键词过滤
        if (contentFilterService.containsSensitiveWords(request.getContent())) {
            throw new RuntimeException("评价内容包含敏感词，请修改后重新提交");
        }
        
        // 2. 获取用户信息
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 3. 创建评价
        RestaurantReview review = new RestaurantReview();
        review.setUserId(userId);
        review.setPoiId(request.getPoiId());
        review.setRestaurantName(request.getRestaurantName());
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        
        // 4. 判断是否认证评论家
        review.setIsCertifiedReview(user.isCertifiedCritic());
        
        // 5. 先发后审：直接发布
        review.setStatus(1);
        
        // 6. 保存
        RestaurantReview saved = reviewRepository.save(review);
        
        log.info("User {} created review for restaurant {}", userId, request.getRestaurantName());
        
        // 7. TODO: 保存图片（如果有）
        
        return convertToDTO(saved, user);
    }
    
    /**
     * 获取餐厅评价列表
     */
    public Page<ReviewDTO> getRestaurantReviews(String poiId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<RestaurantReview> reviews = reviewRepository
            .findByPoiIdAndStatusOrderByCreatedAtDesc(poiId, 1, pageable);
        
        return reviews.map(review -> {
            User user = userRepository.findById(review.getUserId()).orElse(null);
            return convertToDTO(review, user);
        });
    }
    
    /**
     * 获取用户评价列表
     */
    public Page<ReviewDTO> getUserReviews(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<RestaurantReview> reviews = reviewRepository
            .findByUserIdAndStatusOrderByCreatedAtDesc(userId, 1, pageable);
        
        return reviews.map(review -> {
            User user = userRepository.findById(userId).orElse(null);
            return convertToDTO(review, user);
        });
    }
    
    /**
     * 删除评价
     */
    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        RestaurantReview review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("评价不存在"));
        
        if (!review.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除他人的评价");
        }
        
        // 软删除
        review.setStatus(0);
        review.setDeletedAt(java.time.LocalDateTime.now());
        
        log.info("User {} deleted review {}", userId, reviewId);
    }
    
    /**
     * 计算平均评分
     */
    public Double getAverageRating(String poiId) {
        return reviewRepository.calculateAverageRating(poiId);
    }
    
    /**
     * Entity转DTO（公开方法供AdminController使用）
     */
    public ReviewDTO convertToDTO(RestaurantReview review, User user) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setPoiId(review.getPoiId());
        dto.setRestaurantName(review.getRestaurantName());
        dto.setRating(review.getRating());
        dto.setContent(review.getContent());
        dto.setHelpfulCount(review.getHelpfulCount());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setImages(new ArrayList<>());  // TODO: 从review_images表加载
        
        if (user != null) {
            ReviewDTO.ReviewUser reviewUser = new ReviewDTO.ReviewUser();
            reviewUser.setId(user.getId());
            reviewUser.setUsername(user.getUsername());
            reviewUser.setNickname(user.getNickname());
            reviewUser.setAvatarUrl(user.getAvatarUrl());
            reviewUser.setIsCertified(review.getIsCertifiedReview());
            dto.setUser(reviewUser);
        }
        
        return dto;
    }
}

