package com.tastefinder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 内容过滤服务
 * 
 * 实现先发后审机制的关键词过滤
 */
@Service
@Slf4j
public class ContentFilterService {
    
    // 敏感词列表（示例）
    private static final List<String> SENSITIVE_WORDS = Arrays.asList(
        "广告", "推广", "微信", "电话", "联系方式",
        "垃圾", "欺骗", "虚假", "差评"
        // 实际应该从数据库或配置文件加载
    );
    
    /**
     * 检查内容是否包含敏感词
     */
    public boolean containsSensitiveWords(String content) {
        if (content == null || content.isEmpty()) {
            return false;
        }
        
        String lowerContent = content.toLowerCase();
        
        for (String word : SENSITIVE_WORDS) {
            if (lowerContent.contains(word.toLowerCase())) {
                log.warn("Content contains sensitive word: {}", word);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 过滤敏感词（替换为***）
     */
    public String filterSensitiveWords(String content) {
        if (content == null) {
            return null;
        }
        
        String filtered = content;
        
        for (String word : SENSITIVE_WORDS) {
            filtered = filtered.replaceAll("(?i)" + word, "***");
        }
        
        return filtered;
    }
}

