package com.chen.service;

import com.chen.common.RequestHolder;
import com.chen.constant.CacheKeyConstant;
import com.chen.dao.SysPermissionMapper;
import com.chen.dao.SysRolePermissionMapper;
import com.chen.dao.SysRoleUserMapper;
import com.chen.model.SysPermission;
import com.chen.model.SysUser;
import com.chen.util.JsonMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户-权限&&角色-权限Service
 * @Author LeifChen
 * @Date 2018-06-05
 */
@Service
public class SysCoreService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Resource
    private SysCacheService sysCacheService;

    /**
     * 获取当前用户的权限点列表
     * @return
     */
    public List<SysPermission> getCurrentUserPermissionList() {
        int userId = RequestHolder.getCurrentUser().getId();
        return getUserPermissionList(userId);
    }

    /**
     * 获取指定角色的权限点列表
     * @param roleId
     * @return
     */
    public List<SysPermission> getRolePermissionList(int roleId) {
        List<Integer> permissionIdList = sysRolePermissionMapper.getPermissionIdListByRoleIdList(Lists.newArrayList(roleId));
        if (CollectionUtils.isEmpty(permissionIdList)) {
            return Lists.newArrayList();
        }

        return sysPermissionMapper.getByIdList(permissionIdList);
    }

    /**
     * 获取指定用户的权限点列表
     * @param userId 用户id
     * @return
     */
    public List<SysPermission> getUserPermissionList(int userId) {
        if (isSuperAdmin()) {
            return sysPermissionMapper.getAll();
        }

        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }

        List<Integer> userPermissionIdList = sysRolePermissionMapper.getPermissionIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(userPermissionIdList)) {
            return Lists.newArrayList();
        }

        return sysPermissionMapper.getByIdList(userPermissionIdList);
    }

    /**
     * 是否超级管理员
     * @return
     */
    public boolean isSuperAdmin() {
        SysUser sysUser = RequestHolder.getCurrentUser();
        return sysUser.getMail().contains("admin");
    }

    /**
     * 是否拥有访问url的权限
     * @param url
     * @return
     */
    public boolean hasUrlPermission(String url) {
        if (isSuperAdmin()) {
            return true;
        }

        List<SysPermission> permissionList = sysPermissionMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(permissionList)) {
            return true;
        }

        List<SysPermission> userPermissionList = getCurrentUserPermissionListFromCache();
        Set<Integer> userPermissionSet = userPermissionList.stream().map(SysPermission::getId).collect(Collectors.toSet());

        // 规则：只要有一个权限点有权限，就认为有访问权限
        boolean hasValidPermission = false;
        for (SysPermission permission : permissionList) {
            // 无效权限点
            if (permission == null || permission.getStatus() != 1) {
                continue;
            }
            hasValidPermission = true;
            // 判断用户是否具有权限点的访问权限
            if (userPermissionSet.contains(permission.getId())) {
                return true;
            }
        }
        return !hasValidPermission;
    }

    /**
     * 从缓存获取当前用户的权限列表
     * @return
     */
    private List<SysPermission> getCurrentUserPermissionListFromCache() {
        int userId = RequestHolder.getCurrentUser().getId();
        String cacheValue = sysCacheService.getFromCache(CacheKeyConstant.USER_PERMISSION, String.valueOf(userId));
        if (StringUtils.isBlank(cacheValue)) {
            List<SysPermission> permissionList = getCurrentUserPermissionList();
            if (CollectionUtils.isNotEmpty(permissionList)) {
                sysCacheService.saveCache(JsonMapperUtil.obj2String(permissionList), 600, CacheKeyConstant.USER_PERMISSION, String.valueOf(userId));
            }
            return permissionList;
        }
        return JsonMapperUtil.string2Obj(cacheValue, new TypeReference<List<SysPermission>>() {
        });
    }
}
