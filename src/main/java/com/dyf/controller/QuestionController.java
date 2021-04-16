package com.dyf.controller;

import com.dyf.async.EventModel;
import com.dyf.async.EventProducer;
import com.dyf.async.EventType;
import com.dyf.model.Comment;
import com.dyf.model.EntityType;
import com.dyf.model.HostHolder;
import com.dyf.model.Question;
import com.dyf.model.User;
import com.dyf.model.ViewObject;
import com.dyf.service.CommentService;
import com.dyf.service.FollowService;
import com.dyf.service.LikeService;
import com.dyf.service.QuestionService;
import com.dyf.service.UserService;
import com.dyf.util.ZhihuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/3/1
 */
@Controller
public class QuestionController {
    private static final Logger log = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    FollowService followService;

    @RequestMapping(value = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {
        HomeController.pageNum = 0;
        try {
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            if (hostHolder.getUser() == null) {
                question.setUserId(ZhihuUtil.ANONYMOUS_USERID);
                // return ZhihuUtil.getJSONString(999);
            } else {
                question.setUserId(hostHolder.getUser().getId());
            }

            if (questionService.addQuestion(question) > 0) {
                eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION)
                        .setActorId(question.getUserId())
                        .setEntityId(question.getId())
                        .setExt("title", question.getTitle())
                        .setExt("content", question.getContent()));

                return ZhihuUtil.getJSONString(0);
            }
        } catch (Exception e) {
            log.error("增加问题失败" + e.getMessage());
        }
        return ZhihuUtil.getJSONString(1, "失败");
    }


    @RequestMapping(value = "/question/{qid}", method = {RequestMethod.GET})
    public String questionDetail(Model model, @PathVariable("qid") int qid) {
        Question question = questionService.getById(qid);
        model.addAttribute("question", question);

        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<>();
        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);

            // [New Note] : like业务.
            if (hostHolder.getUser() == null) {
                vo.set("liked", 0);
            } else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user", userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments", comments);

        // [NN]: 关注业务.
        List<ViewObject> followUsers = new ArrayList<>();

        // 获取关注的用户信息.   === > 获取最新关注的20个粉丝.
        List<Integer> userIds = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 20);
        for (Integer userId : userIds) {
            ViewObject vo = new ViewObject();
            User u = userService.getUser(userId);

            if (u == null) {
                continue;
            }

            vo.set("name", u.getName());
            vo.set("headUrl", u.getHeadUrl());
            vo.set("id", u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
        } else {
            model.addAttribute("followed", false);
        }
        return "detail";
    }
}
