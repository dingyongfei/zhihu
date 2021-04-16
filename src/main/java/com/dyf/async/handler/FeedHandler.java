package com.dyf.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.dyf.async.EventHandler;
import com.dyf.async.EventModel;
import com.dyf.async.EventType;
import com.dyf.model.EntityType;
import com.dyf.model.Feed;
import com.dyf.model.Question;
import com.dyf.model.User;
import com.dyf.service.FeedService;
import com.dyf.service.FollowService;
import com.dyf.service.QuestionService;
import com.dyf.service.UserService;
import com.dyf.util.JedisAdapter;
import com.dyf.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/3/1
 */
@Component
public class FeedHandler implements EventHandler {

    @Autowired
    FeedService feedService;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void doHandle(EventModel model) {
        // 为了测试，把model的userId随
        // 机一下
        Random r = new Random();
        model.setActorId(1 + r.nextInt(10));

        // 构造一个新鲜事
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(model.getType().getValue());
        feed.setUserId(model.getActorId());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null) {
            // 不支持的feed
            return;
        }

        feedService.addFeed(feed);

        // 获取所有粉丝
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);

        // 系统队列. 没有登陆时，只能看系统的队列 ?
        followers.add(0);
        for (int follower : followers) {
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey, String.valueOf(follower));
            // 限制最长长度，如果timelineKey的长度过长，就删除后面的新鲜事
        }
    }

    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<>();
        // 触发用户是通用的
        User actor = userService.getUser(model.getActorId());
        if (actor == null) {
            return null;
        }

        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());

        if (model.getType() == EventType.COMMENT ||
                (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)) {
            Question question = questionService.getById(model.getEntityId());
            if (question == null) {
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}
