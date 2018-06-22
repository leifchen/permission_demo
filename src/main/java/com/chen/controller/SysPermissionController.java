package com.chen.controller;

import com.chen.bean.PageQuery;
import com.chen.bean.PageResult;
import com.chen.common.JsonData;
import com.chen.model.SysRole;
import com.chen.service.SysPermissionService;
import com.chen.service.SysRoleService;
import com.chen.vo.PermissionVO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 权限点Controller
 * @Author LeifChen
 * @Date 2018-06-05
 */
@Controller
@RequestMapping("/sys/permission")
public class SysPermissionController {

    @Resource
    private SysPermissionService sysPermissionService;
    @Resource
    private SysRoleService sysRoleService;

    /**
     * 权限点的分页信息
     * @param permissionModuleId
     * @param pageQuery
     * @return
     */
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(@RequestParam("permissionModuleId") int permissionModuleId, PageQuery pageQuery) {
        PageResult result = sysPermissionService.getPageByPermissionModuleId(permissionModuleId, pageQuery);
        return JsonData.success(result);
    }

    /**
     * 保存权限点
     * @param param
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveUser(PermissionVO param) {
        sysPermissionService.save(param);
        return JsonData.success();
    }

    /**
     * 更新权限点
     * @param param
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateUser(PermissionVO param) {
        sysPermissionService.update(param);
        return JsonData.success();
    }

    /**
     * 删除权限点
     * @param param
     * @return
     */
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData deleteUser(PermissionVO param) {
        sysPermissionService.delete(param);
        return JsonData.success();
    }

    /**
     * 根据权限点id查询分配的用户、角色
     * @param permissionId
     * @return
     */
    @RequestMapping("/permissions.json")
    @ResponseBody
    public JsonData permissions(@RequestParam("permissionId") int permissionId) {
        Map<String, Object> map = Maps.newHashMap();
        List<SysRole> roleList = sysRoleService.getRoleListByPermissionId(permissionId);
        map.put("users", roleList);
        map.put("roles", sysRoleService.getUserListByRoleList(roleList));
        return JsonData.success(map);
    }
}
