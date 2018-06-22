package com.chen.controller;

import com.chen.common.JsonData;
import com.chen.dto.PermissionModuleLevelDTO;
import com.chen.service.SysPermissionModuleService;
import com.chen.service.SysTreeService;
import com.chen.vo.PermissionModuleVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限模块Controller
 * @Author LeifChen
 * @Date 2018-05-31
 */
@Controller
@RequestMapping("/sys/permissionModule")
public class SysPermissionModuleController {

    @Resource
    private SysPermissionModuleService sysPermissionModuleService;
    @Resource
    private SysTreeService sysTreeService;

    /**
     * 权限模块页面
     * @return
     */
    @RequestMapping("/permissionModule.page")
    public ModelAndView page() {
        return new ModelAndView("permission");
    }

    /**
     * 权限模块树
     * @return
     */
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree() {
        List<PermissionModuleLevelDTO> dtoList = sysTreeService.permissionModuleTree();
        return JsonData.success(dtoList);
    }

    /**
     * 保存权限模板
     * @param param
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData savePermissionModule(PermissionModuleVO param) {
        sysPermissionModuleService.save(param);
        return JsonData.success();
    }

    /**
     * 更新权限模块
     * @param param
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updatePermissionModule(PermissionModuleVO param) {
        sysPermissionModuleService.update(param);
        return JsonData.success();
    }

    /**
     * 删除权限模块
     * @param id
     * @return
     */
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData deletePermissionModule(@RequestParam("id") int id) {
        sysPermissionModuleService.delete(id);
        return JsonData.success();
    }
}
