package com.dyf.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/3/1
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
