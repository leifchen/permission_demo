package com.chen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 后台管理Controller
 * @Author LeifChen
 * @Date 2018-05-29
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     * 后台管理员页面
     * @return
     */
    @RequestMapping("/index.page")
    public ModelAndView index(){
        return new ModelAndView("admin");
    }
}
