package com.tastefinder.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tastefinder.dto.Restaurant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 高德地图POI搜索服务
 * 
 * 调用高德Web服务API获取餐厅POI数据
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AMapPOIService {
    
    @Value("${amap.key}")
    private String amapKey;
    
    @Value("${amap.base-url}")
    private String amapBaseUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 搜索附近餐厅
     * 
     * @param latitude 纬度
     * @param longitude 经度
     * @param radius 搜索半径（米）
     * @return 餐厅列表
     */
    @Cacheable(value = "poi_search", key = "#latitude + ':' + #longitude + ':' + #radius")
    public List<Restaurant> searchNearby(double latitude, double longitude, int radius) {
        log.info("Searching restaurants near: ({}, {}), radius: {}m", latitude, longitude, radius);
        
        try {
            // 构造高德API请求URL
            String url = UriComponentsBuilder.fromHttpUrl(amapBaseUrl + "/place/around")
                .queryParam("key", amapKey)
                .queryParam("location", longitude + "," + latitude)  // 高德API: 经度,纬度
                .queryParam("radius", radius)
                .queryParam("types", "餐饮服务")
                .queryParam("offset", 20)
                .queryParam("page", 1)
                .queryParam("extensions", "all")
                .build()
                .toUriString();
            
            // 调用高德API
            String response = restTemplate.getForObject(url, String.class);
            
            // 解析响应
            return parseAMapResponse(response, latitude, longitude);
            
        } catch (Exception e) {
            log.error("Failed to search restaurants from AMap API", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 解析高德API响应
     */
    private List<Restaurant> parseAMapResponse(String jsonResponse, double userLat, double userLon) {
        List<Restaurant> restaurants = new ArrayList<>();
        
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode pois = root.get("pois");
            
            if (pois != null && pois.isArray()) {
                for (JsonNode poi : pois) {
                    Restaurant restaurant = convertPOIToRestaurant(poi, userLat, userLon);
                    restaurants.add(restaurant);
                }
            }
            
            log.info("Parsed {} restaurants from AMap response", restaurants.size());
            
        } catch (Exception e) {
            log.error("Failed to parse AMap response", e);
        }
        
        return restaurants;
    }
    
    /**
     * 转换高德POI为Restaurant对象
     */
    private Restaurant convertPOIToRestaurant(JsonNode poi, double userLat, double userLon) {
        Restaurant restaurant = new Restaurant();
        
        // 基本信息
        restaurant.setPoiId(poi.get("id").asText());
        restaurant.setName(poi.get("name").asText());
        restaurant.setAddress(poi.has("address") ? poi.get("address").asText() : "");
        
        // 位置信息
        String location = poi.get("location").asText();
        String[] coords = location.split(",");
        Restaurant.Location loc = new Restaurant.Location();
        loc.setLongitude(new BigDecimal(coords[0]));
        loc.setLatitude(new BigDecimal(coords[1]));
        restaurant.setLocation(loc);
        
        // 计算距离
        double distance = calculateDistance(
            userLat, userLon,
            Double.parseDouble(coords[1]), Double.parseDouble(coords[0])
        );
        restaurant.setDistance(distance);
        
        // 其他信息
        if (poi.has("tel")) {
            restaurant.setPhone(poi.get("tel").asText());
        }
        
        if (poi.has("type")) {
            restaurant.setCategory(poi.get("type").asText());
        }
        
        // 评分从高德API获取（如果有）
        if (poi.has("biz_ext")) {
            JsonNode bizExt = poi.get("biz_ext");
            if (bizExt.has("rating") && !bizExt.get("rating").asText().isEmpty()) {
                try {
                    double rating = Double.parseDouble(bizExt.get("rating").asText());
                    restaurant.setRating(BigDecimal.valueOf(rating));
                } catch (Exception e) {
                    log.debug("Failed to parse rating from AMap API");
                }
            }
        }
        
        // 如果高德API没有评分，设置为null（后续可以用用户评价的平均分）
        if (restaurant.getRating() == null) {
            restaurant.setRating(null);
        }
        
        return restaurant;
    }
    
    /**
     * 计算两点之间的距离（Haversine公式）
     * 
     * @return 距离（米）
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // 地球半径（米）
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
}

