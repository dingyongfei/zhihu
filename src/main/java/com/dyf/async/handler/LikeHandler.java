package com.dyf.async.handler;

import com.dyf.async.EventCollectHandler;
import com.dyf.async.EventHandler;
import com.dyf.async.EventModel;
import com.dyf.async.EventType;
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
public class LikeHandler extends EventCollectHandler {

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
        // 注意，这里不能从hostHolder中取，因为已经是另一个用户在登陆了.
        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName()
                + "赞了你的评论,http://127.0.0.1:8080/question/" + model.getExt("questionId"));

        messageService.addMessage(message);
    }
}
