package com.chen.controller;

import com.chen.common.JsonData;
import com.chen.model.SysUser;
import com.chen.service.*;
import com.chen.util.StringUtil;
import com.chen.vo.RoleVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色Controller
 * @Author LeifChen
 * @Date 2018-06-05
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysTreeService sysTreeService;
    @Resource
    private SysRolePermissionService sysRolePermissionService;
    @Resource
    private SysRoleUserService sysRoleUserService;
    @Resource
    private SysUserService sysUserService;

    /**
     * 角色页面
     * @return
     */
    @RequestMapping("/role.page")
    public ModelAndView page() {
        return new ModelAndView("role");
    }

    /**
     * 角色列表
     * @return
     */
    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData list() {
        return JsonData.success(sysRoleService.getAll());
    }

    /**
     * 角色树
     * @param roleId 角色id
     * @return
     */
    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JsonData roleTree(@RequestParam("roleId") int roleId) {
        return JsonData.success(sysTreeService.roleTree(roleId));
    }

    /**
     * 用户列表
     * @param roleId 角色id
     * @return
     */
    @RequestMapping("/users.json")
    @ResponseBody
    public JsonData users(@RequestParam("roleId") int roleId) {
        List<SysUser> selectedUserList = sysRoleUserService.getUserListByRoleId(roleId);
        List<SysUser> allUserList = sysUserService.getAll();
        List<SysUser> unselectedUserList = Lists.newArrayList();
        Set<Integer> selectedUserIdSet = selectedUserList.stream().map(SysUser::getId).collect(Collectors.toSet());
        for (SysUser sysUser : allUserList) {
            if (sysUser.getStatus() == 1 && !selectedUserIdSet.contains(sysUser.getId())) {
                unselectedUserList.add(sysUser);
            }
        }
        selectedUserList = selectedUserList.stream().filter(sysUser -> sysUser.getStatus() == 1).collect(Collectors.toList());
        Map<String, List<SysUser>> map = Maps.newHashMap();
        map.put("selected", selectedUserList);
        map.put("unselected", unselectedUserList);

        return JsonData.success(map);
    }

    /**
     * 保存角色
     * @param param
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveDept(RoleVO param) {
        sysRoleService.save(param);
        return JsonData.success();
    }

    /**
     * 更新角色
     * @param param
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateDept(RoleVO param) {
        sysRoleService.update(param);
        return JsonData.success();
    }

    /**
     * 删除角色
     * @param param
     * @return
     */
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData deleteDept(RoleVO param) {
        sysRoleService.delete(param);
        return JsonData.success();
    }

    /**
     * 分配角色-权限
     * @param roleId        角色id
     * @param permissionIds 权限ids
     * @return
     */
    @RequestMapping("/changePermissions.json")
    @ResponseBody
    public JsonData changePermissions(@RequestParam("roleId") int roleId,
                                      @RequestParam(value = "permissionIds", required = false, defaultValue = "") String permissionIds) {
        List<Integer> permissionIdList = StringUtil.splitToListInt(permissionIds);
        sysRolePermissionService.changeRolePermissions(roleId, permissionIdList);
        return JsonData.success();
    }

    /**
     * 分配角色-用户
     * @param roleId        角色id
     * @param userIds 用户ids
     * @return
     */
    @RequestMapping("/changeUsers.json")
    @ResponseBody
    public JsonData changeUsers(@RequestParam("roleId") int roleId,
                                      @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        sysRoleUserService.changeRoleUsers(roleId, userIdList);
        return JsonData.success();
    }
}
