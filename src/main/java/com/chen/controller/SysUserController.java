package com.chen.controller;

import com.chen.bean.PageQuery;
import com.chen.bean.PageResult;
import com.chen.common.JsonData;
import com.chen.service.SysRoleService;
import com.chen.service.SysTreeService;
import com.chen.service.SysUserService;
import com.chen.vo.UserVO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 后台用户Controller
 * @Author LeifChen
 * @Date 2018-05-29
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysTreeService sysTreeService;

    /**
     * 无权限访问页面
     * @return
     */
    @RequestMapping("/noAuth.page")
    public ModelAndView noAuth(){
        return new ModelAndView("noAuth");
    }

    /**
     * 用户的分页信息
     * @param deptId
     * @param pageQuery
     * @return
     */
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(@RequestParam("deptId") int deptId, PageQuery pageQuery) {
        PageResult result = sysUserService.getPageByDeptId(deptId, pageQuery);
        return JsonData.success(result);
    }

    /**
     * 保存用户
     * @param param
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveUser(UserVO param) {
        sysUserService.save(param);
        return JsonData.success();
    }

    /**
     * 更新用户
     * @param param
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateUser(UserVO param) {
        sysUserService.update(param);
        return JsonData.success();
    }

    /**
     * 删除用户
     * @param param
     * @return
     */
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData deleteUser(UserVO param) {
        sysUserService.delete(param);
        return JsonData.success();
    }

    /**
     * 根据用户id查询分配的权限
     * @param userId 用户id
     * @return
     */
    @RequestMapping("/permissions.json")
    @ResponseBody
    public JsonData permissions(@RequestParam("userId") int userId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("permissions", sysTreeService.userPermissionTree(userId));
        map.put("roles", sysRoleService.getRoleListByUserId(userId));
        return JsonData.success(map);
    }
}
