package com.chen.controller;

import com.chen.bean.PageQuery;
import com.chen.common.JsonData;
import com.chen.service.SysLogService;
import com.chen.vo.LogVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * SysLogController
 * @Author LeifChen
 * @Date 2018-06-12
 */
@Controller
@RequestMapping("/sys/log")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    /**
     * 日志页面
     * @return
     */
    @RequestMapping("/log.page")
    public ModelAndView page() {
        return new ModelAndView("log");
    }

    /**
     * 分页查询
     * @param param
     * @param page
     * @return
     */
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData searchPage(LogVO param, PageQuery page) {
        return JsonData.success(sysLogService.searchPageList(param, page));
    }

    /**
     * 还原
     * @param id
     * @return
     */
    @RequestMapping("/recover.json")
    @ResponseBody
    public JsonData recover(@RequestParam("id") int id) {
        sysLogService.recover(id);
        return JsonData.success();
    }
}
