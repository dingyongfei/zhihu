package com.dyf.controller;

import com.dyf.async.EventModel;
import com.dyf.async.EventProducer;
import com.dyf.async.EventType;
import com.dyf.model.Comment;
import com.dyf.model.EntityType;
import com.dyf.model.HostHolder;
import com.dyf.service.CommentService;
import com.dyf.service.LikeService;
import com.dyf.util.ZhihuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/3/1
 */
@Controller
public class LikeController {

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    EventProducer eventProducer;


    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return ZhihuUtil.getJSONString(999);
        }

        // like
        Comment comment = commentService.getCommentById(commentId);
//        eventProducer.fireEvent(new EventModel(EventType.LIKE)
//                .setActorId(hostHolder.getUser().getId())
//                .setEntityId(commentId)
//                .setEntityType(EntityType.ENTITY_COMMENT)
//                .setEntityOwnerId(comment.getUserId())
//                .setExt("questionId", String.valueOf(comment.getEntityId())));

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ZhihuUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return ZhihuUtil.getJSONString(999);
        }

        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ZhihuUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
