package com.dyf.async;

import com.alibaba.fastjson.JSON;
import com.dyf.util.JedisAdapter;
import com.dyf.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/3/4
 */
@Component
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType, ArrayList<EventHandler>> config = new HashMap<EventType, ArrayList<EventHandler>>();
    private static ApplicationContext applicationContext;


/*    @Autowired
    JedisAdapter jedisAdapter;*/

    @Autowired
    EventQueue eventQueue;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);

        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!eventQueue.isEmpty()) {
/*                        String key = RedisKeyUtil.getEventQueueKey();
                        List<String> events = jedisAdapter.brpop(0, key);*/

                        EventModel eventModel = eventQueue.take();

/*                        for (String message : events) {
                            if (message.equals(key)) {
                                continue;
                            }*/

/*
                            EventModel eventModel = JSON.parseObject(message, EventModel.class);
*/
                            if (!config.containsKey(eventModel.getType())) {
                                log.error("?????????????????????");
/*
                                continue;
*/
                            } else {

                                for (EventHandler handler : config.get(eventModel.getType())) {
                                    handler.doHandle(eventModel);
                                }
                            }
                        }
                    }
                }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
