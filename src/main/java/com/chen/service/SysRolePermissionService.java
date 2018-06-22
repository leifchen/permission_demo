package com.chen.service;

import com.chen.common.RequestHolder;
import com.chen.constant.LogType;
import com.chen.dao.SysLogMapper;
import com.chen.dao.SysRolePermissionMapper;
import com.chen.model.SysLogWithBlobs;
import com.chen.model.SysRolePermission;
import com.chen.util.IpUtil;
import com.chen.util.JsonMapperUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 角色-权限Service
 * @Author LeifChen
 * @Date 2018-06-07
 */
@Service
public class SysRolePermissionService {

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Resource
    private SysLogMapper sysLogMapper;

    /**
     * 分配角色-权限
     * @param roleId
     * @param permissionIdList
     */
    public void changeRolePermissions(Integer roleId, List<Integer> permissionIdList) {
        List<Integer> originPermissionIdList = sysRolePermissionMapper.getPermissionIdListByRoleIdList(Lists.newArrayList(roleId));
        if (originPermissionIdList.size() == permissionIdList.size()) {
            Set<Integer> orginPermissionIdSet = Sets.newHashSet(originPermissionIdList);
            Set<Integer> permissionIdSet = Sets.newHashSet(permissionIdList);
            orginPermissionIdSet.removeAll(permissionIdSet);
            if (CollectionUtils.isEmpty(orginPermissionIdSet)) {
                return;
            }
        }

        updateRolePermissions(roleId,permissionIdList);
        saveRolePermissionLog(roleId,originPermissionIdList,permissionIdList);
    }

    /**
     * 更新角色-权限
     * @param roleId
     * @param permissionIdList
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRolePermissions(int roleId, List<Integer> permissionIdList) {
        sysRolePermissionMapper.deleteByRoleId(roleId);
        if(CollectionUtils.isEmpty(permissionIdList)) {
            return;
        }

        List<SysRolePermission> rolePermissionList = Lists.newArrayList();
        for(Integer permissionId: permissionIdList) {
            SysRolePermission rolePermission = SysRolePermission.builder()
                    .roleId(roleId)
                    .permissionId(permissionId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()))
                    .operateTime(new Date())
                    .build();
            rolePermissionList.add(rolePermission);
        }
        sysRolePermissionMapper.batchInsert(rolePermissionList);
    }

    private void saveRolePermissionLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBlobs sysLog = new SysLogWithBlobs();
        sysLog.setType(LogType.TYPE_ROLE_PERMISSION);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapperUtil.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapperUtil.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }
}
