package com.dyf.async.handler;

import com.dyf.async.EventHandler;
import com.dyf.async.EventModel;
import com.dyf.async.EventType;
import com.dyf.controller.LoginController;
import com.dyf.util.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/3/4
 */
@Component
public class LoginExceptionHandler implements EventHandler {

    private static final Logger log = LoggerFactory.getLogger(LoginExceptionHandler.class);

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        // xxx判断发现这个用户登陆异常
        Map<String, Object> map = new HashMap<>();
        map.put("username", model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"), "登陆IP异常",
                "mails/login_exception.html", map);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
