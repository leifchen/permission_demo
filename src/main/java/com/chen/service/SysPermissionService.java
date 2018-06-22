package com.chen.service;

import com.chen.bean.PageQuery;
import com.chen.bean.PageResult;
import com.chen.common.RequestHolder;
import com.chen.dao.SysPermissionMapper;
import com.chen.exception.ValidateException;
import com.chen.model.SysPermission;
import com.chen.util.BeanValidatorUtil;
import com.chen.util.IpUtil;
import com.chen.vo.PermissionVO;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 权限点Service
 * @Author LeifChen
 * @Date 2018-06-05
 */
@Service
public class SysPermissionService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private SysLogService sysLogService;

    /**
     * 新增
     * @param param
     */
    public void save(PermissionVO param) {
        BeanValidatorUtil.check(param);
        if (checkExist(param.getPermissionModuleId(), param.getName(), param.getId())) {
            throw new ValidateException("当前权限模块下存在相同名称的权限点");
        }
        SysPermission permission = SysPermission.builder()
                .name(param.getName())
                .permissionModuleId(param.getPermissionModuleId())
                .url(param.getUrl())
                .type(param.getType())
                .status(param.getStatus())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();
        permission.setCode(generateCode());
        permission.setOperator(RequestHolder.getCurrentUser().getUsername());
        permission.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        permission.setOperateTime(new Date());
        sysPermissionMapper.insertSelective(permission);
        sysLogService.savePermissionLog(null, permission);
    }

    /**
     * 更新
     * @param param
     */
    public void update(PermissionVO param) {
        BeanValidatorUtil.check(param);
        if (checkExist(param.getPermissionModuleId(), param.getName(), param.getId())) {
            throw new ValidateException("当前权限模块下存在相同名称的权限点");
        }
        SysPermission before = sysPermissionMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限点不存在");
        SysPermission after = SysPermission.builder()
                .id(param.getId())
                .name(param.getName())
                .permissionModuleId(param.getPermissionModuleId())
                .url(param.getUrl())
                .type(param.getType())
                .status(param.getStatus())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysPermissionMapper.updateByPrimaryKeySelective(after);
        sysLogService.savePermissionLog(before, after);
    }

    /**
     * 删除
     * @param param
     */
    public void delete(PermissionVO param) {
        sysPermissionMapper.deleteByPrimaryKey(param.getId());
    }

    /**
     * 根据id分页查询
     * @param permissionModuleId
     * @param page
     * @return
     */
    public PageResult<SysPermission> getPageByPermissionModuleId(int permissionModuleId, PageQuery page) {
        BeanValidatorUtil.check(page);
        int count = sysPermissionMapper.countByPermissionModuleId(permissionModuleId);
        if (count > 0) {
            List<SysPermission> list = sysPermissionMapper.getPageByPermissionModuleId(permissionModuleId, page);
            return PageResult.<SysPermission>builder()
                    .total(count)
                    .data(list)
                    .build();
        }

        return PageResult.<SysPermission>builder().build();
    }

    /**
     * 检查当前权限模块下是否存在相同名称的权限点
     * @param permissionModuleId 权限模块id
     * @param name               权限点名称
     * @param permissionId       权限点id
     * @return
     */
    private boolean checkExist(Integer permissionModuleId, String name, Integer permissionId) {
        return sysPermissionMapper.countByNameAndPermissionModuleId(permissionModuleId, name, permissionId) > 0;
    }

    /**
     * 自动生成权限码
     * @return
     */
    private String generateCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(new Date()) + "_" + new Random().nextInt(100);
    }
}
