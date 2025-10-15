package com.tastefinder.controller;

import com.tastefinder.dto.CreateReviewRequest;
import com.tastefinder.dto.ReviewDTO;
import com.tastefinder.service.ReviewService;
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
 * 评价控制器
 * 
 * 处理餐厅评价相关请求
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review", description = "餐厅评价相关接口")
@SecurityRequirement(name = "BearerAuth")
public class ReviewController {
    
    private final ReviewService reviewService;
    
    /**
     * 获取餐厅评价列表
     */
    @GetMapping
    @Operation(summary = "获取餐厅评价列表", description = "获取指定餐厅的所有评价")
    public ResponseEntity<Map<String, Object>> getReviews(
            @RequestParam String poiId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ReviewDTO> reviews = reviewService.getRestaurantReviews(poiId, page, size);
        Double avgRating = reviewService.getAverageRating(poiId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", reviews.getContent());
        response.put("pagination", Map.of(
            "page", page,
            "size", size,
            "total", reviews.getTotalElements(),
            "totalPages", reviews.getTotalPages()
        ));
        response.put("summary", Map.of(
            "totalCount", reviews.getTotalElements(),
            "averageRating", avgRating != null ? avgRating : 0.0
        ));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 发表评价
     */
    @PostMapping
    @Operation(summary = "发表评价", description = "用户发表餐厅评价（先发后审）")
    public ResponseEntity<ReviewDTO> createReview(
            Authentication authentication,
            @Valid @RequestBody CreateReviewRequest request
    ) {
        Long userId = (Long) authentication.getPrincipal();
        
        ReviewDTO review = reviewService.createReview(userId, request);
        
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }
    
    /**
     * 获取用户评价列表
     */
    @GetMapping("/my")
    @Operation(summary = "获取我的评价", description = "获取当前用户的所有评价")
    public ResponseEntity<Map<String, Object>> getMyReviews(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long userId = (Long) authentication.getPrincipal();
        
        Page<ReviewDTO> reviews = reviewService.getUserReviews(userId, page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", reviews.getContent());
        response.put("pagination", Map.of(
            "page", page,
            "size", size,
            "total", reviews.getTotalElements()
        ));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 删除评价
     */
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "删除评价", description = "删除自己的评价")
    public ResponseEntity<Void> deleteReview(
            Authentication authentication,
            @PathVariable Long reviewId
    ) {
        Long userId = (Long) authentication.getPrincipal();
        
        reviewService.deleteReview(userId, reviewId);
        
        return ResponseEntity.noContent().build();
    }
}

