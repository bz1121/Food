package com.tastefinder.dto;

import lombok.Data;

import java.util.List;

/**
 * 导航路径DTO
 */
@Data
public class NavigationRoute {
    
    /**
     * 总距离（米）
     */
    private Double distance;
    
    /**
     * 预计时长（秒）
     */
    private Integer duration;
    
    /**
     * 出行方式
     */
    private String travelMode;
    
    /**
     * 路径坐标串（编码）
     */
    private String polyline;
    
    /**
     * 导航步骤
     */
    private List<NavigationStep> steps;
    
    @Data
    public static class NavigationStep {
        private String instruction;
        private Double distance;
        private Integer duration;
    }
}

