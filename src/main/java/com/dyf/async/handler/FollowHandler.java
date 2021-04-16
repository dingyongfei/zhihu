package com.dyf.async.handler;

import com.dyf.async.EventHandler;
import com.dyf.async.EventModel;
import com.dyf.async.EventType;
import com.dyf.model.EntityType;
import com.dyf.model.Message;
import com.dyf.model.User;
import com.dyf.service.MessageService;
import com.dyf.service.UserService;
import com.dyf.util.ZhihuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/3/4
 */
@Component
public class FollowHandler implements EventHandler {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(ZhihuUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());

        if (model.getEntityType() == EntityType.ENTITY_QUESTION) {
            message.setContent("用户" + user.getName()
            + "关注了你的问题,http:127.0.0.1:8080/question/" + model.getEntityId());
        } else if (model.getEntityType() == EntityType.ENTITY_USER) {
            message.setContent("用户" + user.getName()
                    + "关注了你,http://127.0.0.1:8080/user/" + model.getActorId());
        }

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW, EventType.UNFOLLOW);
    }
}
