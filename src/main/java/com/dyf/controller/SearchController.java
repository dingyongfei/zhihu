package com.dyf.controller;

import com.dyf.model.EntityType;
import com.dyf.model.Question;
import com.dyf.model.ViewObject;
import com.dyf.service.FollowService;
import com.dyf.service.QuestionService;
import com.dyf.service.SearchService;
import com.dyf.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/3/1
 */
@Controller
public class SearchController {
    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    SearchService searchService;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FollowService followService;

    @RequestMapping(path = {"/search"}, method = {RequestMethod.GET})
    public String search(Model model,
                         @RequestParam("q") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "count", defaultValue = "10") int count) {
        try {
            List<Question> questionList = searchService.searchQuestion(keyword, offset, count, "<em>", "</em>");

            List<ViewObject> vos = new ArrayList<>();

            for (Question question : questionList) {
                Question q = questionService.getById(question.getId());
                ViewObject vo = new ViewObject();

                if (question.getContent() != null) {
                    q.setContent(question.getContent());
                }

                if (question.getTitle() != null) {
                    q.setTitle(question.getTitle());
                }
                vo.set("question", q);
                vo.set("user", userService.getUser(q.getUserId()));
                vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
                vos.add(vo);
            }

            model.addAttribute("vos", vos);
            // keyword字段, 将搜索关键字在搜索框中显示出来.
            model.addAttribute("keyword", keyword);

        } catch (Exception e) {
            log.error("搜索问题失败" + e.getMessage());
        }
        return "result";
    }
}
