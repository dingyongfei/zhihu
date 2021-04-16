package com.dyf.async;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/5/15
 */

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public abstract class EventCollectHandler implements EventHandler {

    @Override
    public void doHandle(EventModel model) {
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
