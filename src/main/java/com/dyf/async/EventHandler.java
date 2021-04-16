package com.dyf.async;

import java.util.List;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/3/4
 */
public interface EventHandler {
    void doHandle(EventModel model);

    List<EventType> getSupportEventTypes();
}
