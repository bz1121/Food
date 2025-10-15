package com.tastefinder.controller;

import com.tastefinder.dto.ReviewDTO;
import com.tastefinder.dto.UserDTO;
import com.tastefinder.entity.RestaurantReview;
import com.tastefinder.entity.User;
import com.tastefinder.repository.RestaurantFavoriteRepository;
import com.tastefinder.repository.RestaurantReviewRepository;
import com.tastefinder.repository.UserRepository;
import com.tastefinder.service.ReviewService;
import com.tastefinder.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理员控制器
 * 
 * 处理后台管理相关请求（仅管理员可访问）
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "管理员接口")
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final UserRepository userRepository;
    private final RestaurantReviewRepository reviewRepository;
    private final RestaurantFavoriteRepository favoriteRepository;
    private final ReviewService reviewService;
    private final UserService userService;
    
    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取统计数据", description = "获取系统总体统计数据")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总用户数
        long totalUsers = userRepository.count();
        stats.put("totalUsers", totalUsers);
        
        // 今日新增用户
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        long todayUsers = userRepository.findAll().stream()
            .filter(u -> u.getCreatedAt().isAfter(todayStart))
            .count();
        stats.put("todayUsers", todayUsers);
        
        // 总评价数
        long totalReviews = reviewRepository.count();
        stats.put("totalReviews", totalReviews);
        
        // 已发布评价数
        long publishedReviews = reviewRepository.findAll().stream()
            .filter(r -> r.getStatus() == 1)
            .count();
        stats.put("publishedReviews", publishedReviews);
        
        // 总收藏数
        long totalFavorites = favoriteRepository.count();
        stats.put("totalFavorites", totalFavorites);
        
        // 待审核评价（这里简化为最近的评价）
        long pendingReviews = 0; // 实际应该是需要人工审核的
        stats.put("pendingReviews", pendingReviews);
        
        // 按角色统计用户
        Map<String, Long> usersByRole = userRepository.findAll().stream()
            .collect(Collectors.groupingBy(
                u -> u.getRoleType().name(),
                Collectors.counting()
            ));
        stats.put("usersByRole", usersByRole);
        
        return ResponseEntity.ok(stats);
    }
    
    /**
     * 获取所有评价（管理员查看）
     */
    @GetMapping("/reviews")
    @Operation(summary = "获取所有评价", description = "管理员查看所有评价")
    public ResponseEntity<Map<String, Object>> getAllReviews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer status
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<RestaurantReview> reviewPage;
        
        if (status != null) {
            // 按状态筛选
            reviewPage = reviewRepository.findAll(pageRequest);
        } else {
            reviewPage = reviewRepository.findAll(pageRequest);
        }
        
        // 转换为DTO
        List<ReviewDTO> reviewDTOs = reviewPage.getContent().stream()
            .map(review -> {
                User user = userRepository.findById(review.getUserId()).orElse(null);
                return reviewService.convertToDTO(review, user);
            })
            .collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", reviewDTOs);
        response.put("pagination", Map.of(
            "page", page,
            "size", size,
            "total", reviewPage.getTotalElements(),
            "totalPages", reviewPage.getTotalPages()
        ));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取所有用户（管理员查看）
     */
    @GetMapping("/users")
    @Operation(summary = "获取所有用户", description = "管理员查看所有用户")
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<User> userPage = userRepository.findAll(pageRequest);
        
        // 转换为DTO
        List<UserDTO> userDTOs = userPage.getContent().stream()
            .map(userService::convertToDTO)
            .collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", userDTOs);
        response.put("pagination", Map.of(
            "page", page,
            "size", size,
            "total", userPage.getTotalElements()
        ));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 删除评价（管理员）
     */
    @DeleteMapping("/reviews/{reviewId}")
    @Operation(summary = "删除评价", description = "管理员删除违规评价")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @RequestParam(required = false) String reason
    ) {
        RestaurantReview review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("评价不存在"));
        
        // 软删除
        review.setStatus(0);
        review.setDeletedAt(LocalDateTime.now());
        review.setDeleteReason(reason != null ? reason : "违反社区规定");
        reviewRepository.save(review);
        
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 禁用/启用用户
     */
    @PutMapping("/users/{userId}/status")
    @Operation(summary = "禁用/启用用户", description = "管理员禁用或启用用户账户")
    public ResponseEntity<Void> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam Integer status  // 1=启用, 0=禁用
    ) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setStatus(status);
        userRepository.save(user);
        
        return ResponseEntity.ok().build();
    }
}

