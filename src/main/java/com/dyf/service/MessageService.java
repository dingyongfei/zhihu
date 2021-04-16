package com.dyf.service;

import com.dyf.dao.MessageDAO;
import com.dyf.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/3/1
 */
@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;

    public int addMessage(Message message) {
        // [注] Message也是有敏感词过滤的
        return messageDAO.addMessage(message) > 0 ? message.getId() : 0;
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    // 1. [New Note] : 注意，要获取conversation list，需要传入userId (即hostHolder的id) !!
    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDAO.getConversationList(userId, offset, limit);
    }

    // (continued) 2. [New Note] : 注意, 同样地，获取一个conversation的未读数字，也需要传入userId (hostHolder的id)
    public int getConversationUnreadCount(int userId, String conversationId) {
        return messageDAO.getConversationUnreadCount(userId, conversationId);
    }

    // c6.3
    public int updateUnreadCount(int userId, String conversationId) {
        return messageDAO.updateUnreadCount(userId, conversationId);
    }
}
