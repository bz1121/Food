package com.tastefinder.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tastefinder.dto.NavigationRoute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 高德导航服务
 * 
 * 调用高德路径规划API
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AMapNavigationService {
    
    @Value("${amap.key}")
    private String amapKey;
    
    @Value("${amap.base-url}")
    private String amapBaseUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 获取导航路径
     * 
     * @param originLat 起点纬度
     * @param originLon 起点经度
     * @param destLat 终点纬度
     * @param destLon 终点经度
     * @param travelMode 出行方式 (driving, walking, transit)
     * @return 导航路径
     */
    public NavigationRoute getRoute(double originLat, double originLon,
                                    double destLat, double destLon,
                                    String travelMode) {
        log.info("Calculating route: ({},{}) -> ({},{}), mode: {}", 
            originLat, originLon, destLat, destLon, travelMode);
        
        try {
            String apiPath = getApiPath(travelMode);
            
            String url = UriComponentsBuilder.fromHttpUrl(amapBaseUrl + apiPath)
                .queryParam("key", amapKey)
                .queryParam("origin", originLon + "," + originLat)
                .queryParam("destination", destLon + "," + destLat)
                .queryParam("extensions", "all")
                .build()
                .toUriString();
            
            String response = restTemplate.getForObject(url, String.class);
            
            return parseNavigationResponse(response, travelMode);
            
        } catch (Exception e) {
            log.error("Failed to get navigation route from AMap API", e);
            return createFallbackRoute(originLat, originLon, destLat, destLon, travelMode);
        }
    }
    
    /**
     * 根据出行方式选择API端点
     */
    private String getApiPath(String travelMode) {
        return switch (travelMode) {
            case "walking" -> "/direction/walking";
            case "transit" -> "/direction/transit/integrated";
            default -> "/direction/driving";
        };
    }
    
    /**
     * 解析导航API响应
     */
    private NavigationRoute parseNavigationResponse(String jsonResponse, String travelMode) {
        NavigationRoute route = new NavigationRoute();
        route.setTravelMode(travelMode);
        route.setSteps(new ArrayList<>());
        
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode routeNode = root.path("route");
            JsonNode paths = routeNode.path("paths");
            
            if (paths.isArray() && paths.size() > 0) {
                JsonNode firstPath = paths.get(0);
                
                // 距离和时长
                route.setDistance(firstPath.path("distance").asDouble());
                route.setDuration(firstPath.path("duration").asInt());
                
                // Polyline（路径坐标串）
                JsonNode steps = firstPath.path("steps");
                if (steps.isArray()) {
                    StringBuilder polyline = new StringBuilder();
                    for (JsonNode step : steps) {
                        if (step.has("polyline")) {
                            polyline.append(step.get("polyline").asText()).append(";");
                        }
                        
                        // 步骤说明
                        NavigationRoute.NavigationStep navStep = new NavigationRoute.NavigationStep();
                        navStep.setInstruction(step.path("instruction").asText());
                        navStep.setDistance(step.path("distance").asDouble());
                        navStep.setDuration(step.path("duration").asInt());
                        route.getSteps().add(navStep);
                    }
                    route.setPolyline(polyline.toString());
                }
            }
            
        } catch (Exception e) {
            log.error("Failed to parse navigation response", e);
        }
        
        return route;
    }
    
    /**
     * 创建降级路径（API失败时）
     */
    private NavigationRoute createFallbackRoute(double originLat, double originLon,
                                                double destLat, double destLon,
                                                String travelMode) {
        NavigationRoute route = new NavigationRoute();
        route.setTravelMode(travelMode);
        
        // 简单计算直线距离
        double distance = calculateDistance(originLat, originLon, destLat, destLon);
        route.setDistance(distance);
        
        // 估算时长（驾车60km/h，步行5km/h）
        double speedKmh = travelMode.equals("walking") ? 5.0 : 60.0;
        route.setDuration((int) ((distance / 1000.0) / speedKmh * 3600));
        
        route.setPolyline("");
        route.setSteps(new ArrayList<>());
        
        return route;
    }
    
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}

