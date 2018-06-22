package com.chen.service;

import com.chen.common.RequestHolder;
import com.chen.dao.SysDeptMapper;
import com.chen.dao.SysUserMapper;
import com.chen.exception.ValidateException;
import com.chen.model.SysDept;
import com.chen.util.BeanValidatorUtil;
import com.chen.util.IpUtil;
import com.chen.util.LevelUtil;
import com.chen.vo.DeptVO;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 部门Service
 * @Author LeifChen
 * @Date 2018-04-18
 */
@Service
public class SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;

    /**
     * 保存
     * @param param
     */
    public void save(DeptVO param) {
        BeanValidatorUtil.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ValidateException("同一层级下存在相同名称的部门");
        }
        SysDept dept = SysDept.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark()).build();
        dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        dept.setOperator(RequestHolder.getCurrentUser().getUsername());
        dept.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        dept.setOperateTime(new Date());
        sysDeptMapper.insertSelective(dept);
        sysLogService.saveDeptLog(null, dept);
    }

    /**
     * 更新
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(DeptVO param) {
        BeanValidatorUtil.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ValidateException("同一层级下存在相同名称的部门");
        }
        SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的部门不存在");
        SysDept after = SysDept.builder()
                .id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        updateWithChild(before, after);
        sysLogService.saveDeptLog(before, after);
    }

    /**
     * 删除
     * @param deptId 部门id
     */
    public void delete(int deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(dept, "待删除的部门不存在，无法删除");
        if (sysDeptMapper.countByParentId(deptId) > 0) {
            throw new ValidateException("当前部门下面有子部门，无法删除");
        }
        if (sysUserMapper.countByDeptId(deptId) > 0) {
            throw new ValidateException("当前部门下面有用户，无法删除");
        }
        sysDeptMapper.deleteByPrimaryKey(deptId);
    }

    /**
     * 更新包含子层级的部门
     * @param before
     * @param after
     */
    private void updateWithChild(SysDept before, SysDept after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(deptList)) {
                for (SysDept dept : deptList) {
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        sysDeptMapper.updateByPrimaryKey(after);
    }

    /**
     * 检查同一层级下是否存在相同名称的部门
     * @param parentId 父层级id
     * @param deptName 部门名称
     * @param deptId   部门id
     * @return
     */
    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }

    /**
     * 获取层级
     * @param deptId
     * @return
     */
    private String getLevel(Integer deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept == null) {
            return null;
        }

        return dept.getLevel();
    }
}
