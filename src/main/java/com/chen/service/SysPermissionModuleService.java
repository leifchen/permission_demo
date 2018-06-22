package com.chen.service;

import com.chen.common.RequestHolder;
import com.chen.dao.SysPermissionMapper;
import com.chen.dao.SysPermissionModuleMapper;
import com.chen.exception.ValidateException;
import com.chen.model.SysPermissionModule;
import com.chen.util.BeanValidatorUtil;
import com.chen.util.IpUtil;
import com.chen.util.LevelUtil;
import com.chen.vo.PermissionModuleVO;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 权限模块Service
 * @Author LeifChen
 * @Date 2018-05-31
 */
@Service
public class SysPermissionModuleService {

    @Resource
    private SysPermissionModuleMapper sysPermissionModuleMapper;
    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private SysLogService sysLogService;

    /**
     * 新增
     * @param param
     */
    public void save(PermissionModuleVO param) {
        BeanValidatorUtil.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ValidateException("同一层级下存在相同名称的权限模块");
        }
        SysPermissionModule permissionModule = SysPermissionModule.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        permissionModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        permissionModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        permissionModule.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        permissionModule.setOperateTime(new Date());
        sysPermissionModuleMapper.insertSelective(permissionModule);
        sysLogService.savePermissionModuleLog(null, permissionModule);
    }

    /**
     * 更新
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(PermissionModuleVO param) {
        BeanValidatorUtil.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ValidateException("同一层级下存在相同名称的权限模块");
        }
        SysPermissionModule before = sysPermissionModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限模块不存在");
        SysPermissionModule after = SysPermissionModule.builder()
                .id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        updateWithChild(before, after);
        sysLogService.savePermissionModuleLog(before, after);
    }

    /**
     * 删除
     * @param permissionModuleId
     */
    public void delete(int permissionModuleId) {
        SysPermissionModule permissionModule = sysPermissionModuleMapper.selectByPrimaryKey(permissionModuleId);
        Preconditions.checkNotNull(permissionModule, "待删除的权限模块不存在，无法删除");
        if (sysPermissionModuleMapper.countByParentId(permissionModuleId) > 0) {
            throw new ValidateException("当前权限模块下面有子模块，无法删除");
        }
        if (sysPermissionMapper.countByPermissionModuleId(permissionModuleId) > 0) {
            throw new ValidateException("当前权限模块下面有权限点，无法删除");
        }
        sysPermissionModuleMapper.deleteByPrimaryKey(permissionModuleId);
    }

    /**
     * 检查同一层级下是否存在相同名称的权限模块
     * @param parentId
     * @param permissionModuleName
     * @param deptId
     * @return
     */
    private boolean checkExist(Integer parentId, String permissionModuleName, Integer deptId) {
        return sysPermissionModuleMapper.countByNameAndParentId(parentId, permissionModuleName, deptId) > 0;
    }

    /**
     * 获取层级
     * @param permissionModuleId
     * @return
     */
    private String getLevel(Integer permissionModuleId) {
        SysPermissionModule permissionModule = sysPermissionModuleMapper.selectByPrimaryKey(permissionModuleId);
        if (permissionModule == null) {
            return null;
        }

        return permissionModule.getLevel();
    }

    /**
     * 更新包含子层级的权限模块
     * @param before
     * @param after
     */
    private void updateWithChild(SysPermissionModule before, SysPermissionModule after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysPermissionModule> permissionModuleList = sysPermissionModuleMapper.getChildPermissionModuleListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(permissionModuleList)) {
                for (SysPermissionModule permissionModule : permissionModuleList) {
                    String level = permissionModule.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        permissionModule.setLevel(level);
                    }
                }
                sysPermissionModuleMapper.batchUpdateLevel(permissionModuleList);
            }
        }
        sysPermissionModuleMapper.updateByPrimaryKey(after);
    }
}
