package com.chen.controller;

import com.chen.common.JsonData;
import com.chen.dto.DeptLevelDTO;
import com.chen.service.SysDeptService;
import com.chen.service.SysTreeService;
import com.chen.vo.DeptVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门Controller
 * @Author LeifChen
 * @Date 2018-04-18
 */
@Controller
@RequestMapping("/sys/dept")
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private SysTreeService sysTreeService;

    /**
     * 部门页面
     * @return
     */
    @RequestMapping("/dept.page")
    public ModelAndView page() {
        return new ModelAndView("dept");
    }

    /**
     * 部门树
     * @return
     */
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree() {
        List<DeptLevelDTO> dtoList = sysTreeService.deptTree();
        return JsonData.success(dtoList);
    }

    /**
     * 保存部门
     * @param param
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveDept(DeptVO param) {
        sysDeptService.save(param);
        return JsonData.success();
    }

    /**
     * 更新部门
     * @param param
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateDept(DeptVO param) {
        sysDeptService.update(param);
        return JsonData.success();
    }

    /**
     * 删除部门
     * @param id 部门id
     * @return
     */
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData deleteDept(@Param("id") int id) {
        sysDeptService.delete(id);
        return JsonData.success();
    }
}
