package com.dyf.async;

import com.alibaba.fastjson.JSONObject;
import com.dyf.util.JedisAdapter;
import com.dyf.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/3/4
 */

@Component
public class EventProducer {

    @Autowired
    EventQueue eventQueue;

    @Autowired
    ApplicationContext applicationContext;

    public boolean fireEvent(EventModel eventModel) {
        try {
/*            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);*/
            eventQueue.put(eventModel);

            String[] names = applicationContext.getBeanDefinitionNames();
            for (String name : names)
            {
                //System.out.println(">>>>>>" + name);
            }
            //System.out.println("------\nBean count:" + applicationContext.getBeanDefinitionCount());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args)
    {

    }
}