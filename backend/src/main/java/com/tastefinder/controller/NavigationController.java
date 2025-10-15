package com.tastefinder.controller;

import com.tastefinder.dto.NavigationRoute;
import com.tastefinder.service.AMapNavigationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 导航控制器
 * 
 * 处理路径规划相关请求
 */
@RestController
@RequestMapping("/api/navigation")
@RequiredArgsConstructor
@Tag(name = "Navigation", description = "路径规划相关接口")
@SecurityRequirement(name = "BearerAuth")
public class NavigationController {
    
    private final AMapNavigationService navigationService;
    
    /**
     * 获取路径规划
     */
    @GetMapping("/route")
    @Operation(summary = "获取路径规划", description = "计算从起点到终点的导航路径")
    public ResponseEntity<NavigationRoute> getRoute(
            @Parameter(description = "起点纬度") @RequestParam double originLat,
            @Parameter(description = "起点经度") @RequestParam double originLon,
            @Parameter(description = "终点纬度") @RequestParam double destLat,
            @Parameter(description = "终点经度") @RequestParam double destLon,
            @Parameter(description = "出行方式") @RequestParam(defaultValue = "driving") String travelMode
    ) {
        NavigationRoute route = navigationService.getRoute(
            originLat, originLon, destLat, destLon, travelMode
        );
        
        return ResponseEntity.ok(route);
    }
}

