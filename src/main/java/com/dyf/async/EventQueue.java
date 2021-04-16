package com.dyf.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/5/15
 */
@Component
public class EventQueue implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(EventQueue.class);
    private int INIT_CAPACITY = 10;
    private BlockingQueue<EventModel> queue;

    @Override
    public void afterPropertiesSet() throws Exception {
        queue = new ArrayBlockingQueue<>(INIT_CAPACITY);
    }

    public void put(EventModel model) {
        try {
            queue.put(model);
        } catch (InterruptedException e) {
            log.error("an unexpected put error occurred", e);
        }
    }

    public EventModel take() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            log.error("an unexpected take error occurred", e);
        }
        return null;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
