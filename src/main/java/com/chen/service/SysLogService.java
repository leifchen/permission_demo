package com.chen.service;

import com.chen.bean.PageQuery;
import com.chen.bean.PageResult;
import com.chen.common.RequestHolder;
import com.chen.constant.LogType;
import com.chen.dao.*;
import com.chen.dto.LogDTO;
import com.chen.exception.ValidateException;
import com.chen.model.*;
import com.chen.util.BeanValidatorUtil;
import com.chen.util.IpUtil;
import com.chen.util.JsonMapperUtil;
import com.chen.vo.LogVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * SysLogService
 * @Author LeifChen
 * @Date 2018-06-12
 */
@Service
public class SysLogService {

    @Resource
    private SysLogMapper sysLogMapper;
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysPermissionModuleMapper sysPermissionModuleMapper;
    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRolePermissionService sysRolePermissionService;
    @Resource
    private SysRoleUserService sysRoleUserService;

    /**
     * 分页查询
     * @param param
     * @param page
     * @return
     */
    public PageResult<SysLogWithBlobs> searchPageList(LogVO param, PageQuery page) {
        BeanValidatorUtil.check(page);
        LogDTO dto = new LogDTO();
        dto.setType(param.getType());
        if (StringUtils.isNotBlank(param.getBeforeSeg())) {
            dto.setBeforeSeg("%" + param.getBeforeSeg() + "%");
        }
        if (StringUtils.isNotBlank(param.getAfterSeg())) {
            dto.setAfterSeg("%" + param.getAfterSeg() + "%");
        }
        if (StringUtils.isNotBlank(param.getOperator())) {
            dto.setOperator("%" + param.getOperator() + "%");
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isNotBlank(param.getFromTime())) {
                dto.setFromTime(dateFormat.parse(param.getFromTime()));
            }
            if (StringUtils.isNotBlank(param.getToTime())) {
                dto.setToTime(dateFormat.parse(param.getToTime()));
            }
        } catch (Exception e) {
            throw new ValidateException("传入的日期格式有问题，正确格式为:yyyy-MM-dd HH:mm:ss");
        }
        int count = sysLogMapper.countBySearch(dto);
        if (count > 0) {
            List<SysLogWithBlobs> logList = sysLogMapper.getPageListBySearch(dto, page);
            return PageResult.<SysLogWithBlobs>builder()
                    .total(count)
                    .data(logList)
                    .build();
        }
        return PageResult.<SysLogWithBlobs>builder().build();
    }

    /**
     * 还原
     * @param id
     */
    public void recover(int id) {
        SysLogWithBlobs sysLog = sysLogMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(sysLog, "待还原的记录不存在");
        switch (sysLog.getType()) {
            case LogType.TYPE_DEPT:
                SysDept beforeDept = sysDeptMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeDept, "待还原的部门已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ValidateException("新增和删除操作不做还原");
                }
                SysDept afterDept = JsonMapperUtil.string2Obj(sysLog.getOldValue(), new TypeReference<SysDept>() {
                });
                afterDept.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterDept.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
                afterDept.setOperateTime(new Date());
                sysDeptMapper.updateByPrimaryKeySelective(afterDept);
                saveDeptLog(beforeDept, afterDept);
                break;
            case LogType.TYPE_USER:
                SysUser beforeUser = sysUserMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeUser, "待还原的用户已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ValidateException("新增和删除操作不做还原");
                }
                SysUser afterUser = JsonMapperUtil.string2Obj(sysLog.getOldValue(), new TypeReference<SysUser>() {
                });
                afterUser.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterUser.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
                afterUser.setOperateTime(new Date());
                sysUserMapper.updateByPrimaryKeySelective(afterUser);
                saveUserLog(beforeUser, afterUser);
                break;
            case LogType.TYPE_PERMISSION_MODULE:
                SysPermissionModule beforePermissionModule = sysPermissionModuleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforePermissionModule, "待还原的权限模块已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ValidateException("新增和删除操作不做还原");
                }
                SysPermissionModule afterPermissionModule = JsonMapperUtil.string2Obj(sysLog.getOldValue(), new TypeReference<SysPermissionModule>() {
                });
                afterPermissionModule.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterPermissionModule.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
                afterPermissionModule.setOperateTime(new Date());
                sysPermissionModuleMapper.updateByPrimaryKeySelective(afterPermissionModule);
                savePermissionModuleLog(beforePermissionModule, afterPermissionModule);
                break;
            case LogType.TYPE_PERMISSION:
                SysPermission beforePermission = sysPermissionMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforePermission, "待还原的权限点已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ValidateException("新增和删除操作不做还原");
                }
                SysPermission afterPermission = JsonMapperUtil.string2Obj(sysLog.getOldValue(), new TypeReference<SysPermission>() {
                });
                afterPermission.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterPermission.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
                afterPermission.setOperateTime(new Date());
                sysPermissionMapper.updateByPrimaryKeySelective(afterPermission);
                savePermissionLog(beforePermission, afterPermission);
                break;
            case LogType.TYPE_ROLE:
                SysRole beforeRole = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeRole, "待还原的角色已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ValidateException("新增和删除操作不做还原");
                }
                SysRole afterRole = JsonMapperUtil.string2Obj(sysLog.getOldValue(), new TypeReference<SysRole>() {
                });
                afterRole.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterRole.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
                afterRole.setOperateTime(new Date());
                sysRoleMapper.updateByPrimaryKeySelective(afterRole);
                saveRoleLog(beforeRole, afterRole);
                break;
            case LogType.TYPE_ROLE_PERMISSION:
                SysRole rolePermission = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(rolePermission, "角色已经不存在了");
                sysRolePermissionService.changeRolePermissions(sysLog.getTargetId(), JsonMapperUtil.string2Obj(sysLog.getOldValue(), new TypeReference<List<Integer>>() {
                }));
                break;
            case LogType.TYPE_ROLE_USER:
                SysRole roleUser = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(roleUser, "角色已经不存在了");
                sysRoleUserService.changeRoleUsers(sysLog.getTargetId(), JsonMapperUtil.string2Obj(sysLog.getOldValue(), new TypeReference<List<Integer>>() {
                }));
                break;
            default:
        }
    }

    /**
     * 保存部门变更日志
     * @param before
     * @param after
     */
    public void saveDeptLog(SysDept before, SysDept after) {
        SysLogWithBlobs sysLog = new SysLogWithBlobs();
        sysLog.setType(LogType.TYPE_DEPT);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapperUtil.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapperUtil.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }

    /**
     * 保存用户变更日志
     * @param before
     * @param after
     */
    public void saveUserLog(SysUser before, SysUser after) {
        SysLogWithBlobs sysLog = new SysLogWithBlobs();
        sysLog.setType(LogType.TYPE_USER);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapperUtil.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapperUtil.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }

    /**
     * 保存权限模块变更日志
     * @param before
     * @param after
     */
    public void savePermissionModuleLog(SysPermissionModule before, SysPermissionModule after) {
        SysLogWithBlobs sysLog = new SysLogWithBlobs();
        sysLog.setType(LogType.TYPE_PERMISSION_MODULE);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapperUtil.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapperUtil.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }

    /**
     * 保存权限点变更日志
     * @param before
     * @param after
     */
    public void savePermissionLog(SysPermission before, SysPermission after) {
        SysLogWithBlobs sysLog = new SysLogWithBlobs();
        sysLog.setType(LogType.TYPE_PERMISSION);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapperUtil.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapperUtil.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }

    /**
     * 保存角色变更日志
     * @param before
     * @param after
     */
    public void saveRoleLog(SysRole before, SysRole after) {
        SysLogWithBlobs sysLog = new SysLogWithBlobs();
        sysLog.setType(LogType.TYPE_ROLE);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapperUtil.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapperUtil.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }
}
