package com.tastefinder.service;

import com.tastefinder.dto.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * AMapPOIService 单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("高德POI搜索服务测试")
class AMapPOIServiceTest {
    
    @Mock
    private RestTemplate restTemplate;
    
    @InjectMocks
    private AMapPOIService amapPOIService;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(amapPOIService, "amapKey", "test-key");
        ReflectionTestUtils.setField(amapPOIService, "amapBaseUrl", "https://restapi.amap.com/v3");
    }
    
    @Test
    @DisplayName("搜索附近餐厅 - 应该返回餐厅列表")
    void testSearchNearby_Success() {
        // Given
        String mockResponse = """
            {
              "status": "1",
              "pois": [
                {
                  "id": "B001",
                  "name": "测试餐厅",
                  "address": "测试地址",
                  "location": "116.397470,39.908823",
                  "tel": "010-12345678",
                  "type": "餐饮服务"
                }
              ]
            }
            """;
        
        when(restTemplate.getForObject(anyString(), eq(String.class)))
            .thenReturn(mockResponse);
        
        // When
        List<Restaurant> restaurants = amapPOIService.searchNearby(39.908823, 116.397470, 5000);
        
        // Then
        assertNotNull(restaurants);
        assertEquals(1, restaurants.size());
        assertEquals("B001", restaurants.get(0).getPoiId());
        assertEquals("测试餐厅", restaurants.get(0).getName());
        
        verify(restTemplate).getForObject(anyString(), eq(String.class));
    }
    
    @Test
    @DisplayName("API调用失败 - 应该返回空列表")
    void testSearchNearby_APIFailure() {
        // Given
        when(restTemplate.getForObject(anyString(), eq(String.class)))
            .thenThrow(new RuntimeException("API Error"));
        
        // When
        List<Restaurant> restaurants = amapPOIService.searchNearby(39.908823, 116.397470, 5000);
        
        // Then
        assertNotNull(restaurants);
        assertTrue(restaurants.isEmpty());
    }
}

