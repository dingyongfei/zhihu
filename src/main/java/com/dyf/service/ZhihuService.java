package com.dyf.service;

import org.springframework.stereotype.Service;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/3/1
 */
@Service
public class ZhihuService {
    public String getMessage(int userId) {
        return "Hello Message:" + String.valueOf(userId);
    }
}
