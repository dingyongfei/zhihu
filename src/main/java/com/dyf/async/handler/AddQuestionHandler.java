package com.dyf.async.handler;

import com.dyf.async.EventHandler;
import com.dyf.async.EventModel;
import com.dyf.async.EventType;
import com.dyf.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 * @Author: Ding Yongfei
 * @Date: 2020/3/4
 */
@Component
public class AddQuestionHandler implements EventHandler {
    private static final Logger log = LoggerFactory.getLogger(AddQuestionHandler.class);

    @Autowired
    SearchService searchService;

    @Override
    public void doHandle(EventModel model) {
        try {
            searchService.indexQuestion(model.getEntityId(),
                    model.getExt("title"),
                    model.getExt("content"));
        } catch (Exception e) {
            log.error("增加问题索引失败");
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
