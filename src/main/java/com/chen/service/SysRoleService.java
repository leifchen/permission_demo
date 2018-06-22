package com.chen.service;

import com.chen.common.RequestHolder;
import com.chen.dao.SysRoleMapper;
import com.chen.dao.SysRolePermissionMapper;
import com.chen.dao.SysRoleUserMapper;
import com.chen.dao.SysUserMapper;
import com.chen.exception.ValidateException;
import com.chen.model.SysRole;
import com.chen.model.SysUser;
import com.chen.util.BeanValidatorUtil;
import com.chen.util.IpUtil;
import com.chen.vo.RoleVO;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色Service
 * @Author LeifChen
 * @Date 2018-06-05
 */
@Service
public class SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;

    /**
     * 保存
     * @param param
     */
    public void save(RoleVO param) {
        BeanValidatorUtil.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ValidateException("角色名称已经存在");
        }
        SysRole role = SysRole.builder()
                .name(param.getName())
                .type(param.getType())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        role.setOperator(RequestHolder.getCurrentUser().getUsername());
        role.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        role.setOperateTime(new Date());
        sysRoleMapper.insertSelective(role);
        sysLogService.saveRoleLog(null, role);
    }

    /**
     * 更新
     * @param param
     */
    public void update(RoleVO param) {
        BeanValidatorUtil.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ValidateException("角色名称已经存在");
        }
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");
        SysRole after = SysRole.builder()
                .id(param.getId())
                .name(param.getName())
                .type(param.getType())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysRoleMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveRoleLog(before, after);
    }

    /**
     * 删除
     * @param param
     */
    public void delete(RoleVO param) {
        sysRoleMapper.deleteByPrimaryKey(param.getId());
    }

    /**
     * 获取所有角色列表
     * @return
     */
    public List<SysRole> getAll() {
        return sysRoleMapper.getAllRole();
    }

    /**
     * 根据用户id获取角色列表
     * @param userId
     * @return
     */
    public List<SysRole> getRoleListByUserId(int userId) {
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }

        return sysRoleMapper.getByIdList(roleIdList);
    }

    /**
     * 根据权限点id获取角色列表
     * @param permissionId
     * @return
     */
    public List<SysRole> getRoleListByPermissionId(int permissionId) {
        List<Integer> roleIdList = sysRolePermissionMapper.getRoleIdListByPermissionId(permissionId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }

        return sysRoleMapper.getByIdList(roleIdList);
    }

    /**
     * 根据角色列表获取用户列表
     * @param roleList 角色列表
     * @return
     */
    public List<SysUser> getUserListByRoleList(List<SysRole> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }

        List<Integer> roleIdList = roleList.stream().map(SysRole::getId).collect(Collectors.toList());
        List<Integer> userIdList = sysRoleUserMapper.getUseIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }

        return sysUserMapper.getByIdList(userIdList);
    }

    /**
     * 根据name和id判断是否存在记录
     * @param name 角色名称
     * @param id   角色id
     * @return
     */
    private boolean checkExist(String name, Integer id) {
        return sysRoleMapper.countByName(name, id) > 0;
    }

}
