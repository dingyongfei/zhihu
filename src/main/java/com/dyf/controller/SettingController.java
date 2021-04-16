package com.dyf.controller;

import com.dyf.service.ZhihuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/3/1
 */
@Controller
public class SettingController {

    @Autowired
    ZhihuService zhihuService;

    @RequestMapping(path = {"/setting"}, method = {RequestMethod.GET})
    @ResponseBody
    public String setting(HttpSession httpSession) {
        return "Setting OK. " + zhihuService.getMessage(1);
    }
}
