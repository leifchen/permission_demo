package com.chen.service;

import com.chen.common.RequestHolder;
import com.chen.constant.LogType;
import com.chen.dao.SysLogMapper;
import com.chen.dao.SysRoleUserMapper;
import com.chen.dao.SysUserMapper;
import com.chen.model.SysLogWithBlobs;
import com.chen.model.SysRoleUser;
import com.chen.model.SysUser;
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
 * 角色-用户Service
 * @Author LeifChen
 * @Date 2018-06-07
 */
@Service
public class SysRoleUserService {

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogMapper sysLogMapper;

    /**
     * 根据角色id查询用户列表
     * @param roleId
     * @return
     */
    public List<SysUser> getUserListByRoleId(int roleId) {
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }

        return sysUserMapper.getByIdList(userIdList);
    }

    /**
     * 分配角色-用户
     * @param roleId
     * @param userIdList
     */
    public void changeRoleUsers(Integer roleId, List<Integer> userIdList) {
        List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (originUserIdList.size() == userIdList.size()) {
            Set<Integer> orginUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            orginUserIdSet.removeAll(userIdSet);
            if (CollectionUtils.isEmpty(orginUserIdSet)) {
                return;
            }
        }

        updateRoleUsers(roleId, userIdList);
        saveRoleUserLog(roleId, originUserIdList, userIdList);
    }

    /**
     * 更新角色-用户关系
     * @param roleId
     * @param userIdList
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleUsers(Integer roleId, List<Integer> userIdList) {
        sysRoleUserMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }

        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId : userIdList) {
            SysRoleUser roleUser = SysRoleUser.builder()
                    .roleId(roleId)
                    .userId(userId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()))
                    .operateTime(new Date())
                    .build();
            roleUserList.add(roleUser);
        }

        sysRoleUserMapper.batchInsert(roleUserList);
    }

    private void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBlobs sysLog = new SysLogWithBlobs();
        sysLog.setType(LogType.TYPE_ROLE_USER);
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
