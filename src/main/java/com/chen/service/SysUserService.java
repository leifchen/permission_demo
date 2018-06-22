package com.chen.service;

import com.chen.bean.PageQuery;
import com.chen.bean.PageResult;
import com.chen.common.RequestHolder;
import com.chen.dao.SysUserMapper;
import com.chen.exception.ValidateException;
import com.chen.model.SysUser;
import com.chen.util.BeanValidatorUtil;
import com.chen.util.IpUtil;
import com.chen.util.Md5Util;
import com.chen.vo.UserVO;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 用户Service
 * @Author LeifChen
 * @Date 2018-05-29
 */
@Service
public class SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;

    /**
     * 保存
     * @param param
     */
    public void save(UserVO param) {
        BeanValidatorUtil.check(param);
        if (checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ValidateException("电话已被占用");
        }
        if (checkEmailExist(param.getMail(), param.getId())) {
            throw new ValidateException("邮箱已被占用");
        }
        String password = "123456";
        String encryptedPassword = Md5Util.encrypt(password);

        SysUser user = SysUser.builder()
                .username(param.getUsername())
                .password(encryptedPassword)
                .telephone(param.getTelephone())
                .mail(param.getMail())
                .deptId(param.getDeptId())
                .status(param.getStatus())
                .remark(param.getRemark()).build();
        user.setOperator(RequestHolder.getCurrentUser().getUsername());
        user.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        user.setOperateTime(new Date());
        sysUserMapper.insertSelective(user);
        sysLogService.saveUserLog(null, user);
    }

    /**
     * 更新
     * @param param
     */
    public void update(UserVO param) {
        BeanValidatorUtil.check(param);
        if (checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ValidateException("电话已被占用");
        }
        if (checkEmailExist(param.getMail(), param.getId())) {
            throw new ValidateException("邮箱已被占用");
        }
        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");
        SysUser after = SysUser.builder()
                .id(param.getId())
                .username(param.getUsername())
                .telephone(param.getTelephone())
                .mail(param.getMail())
                .deptId(param.getDeptId())
                .status(param.getStatus())
                .remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveUserLog(before, after);
    }

    /**
     * 删除
     * @param param
     */
    public void delete(UserVO param) {
        sysUserMapper.deleteByPrimaryKey(param.getId());
    }

    /**
     * 根据关键字查找用户
     * @param keyword
     * @return
     */
    public SysUser findByKeyword(String keyword) {
        return sysUserMapper.findByKeyword(keyword);
    }

    /**
     * 根据id分页查询
     * @param deptId
     * @param page
     * @return
     */
    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery page){
        BeanValidatorUtil.check(page);
        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0) {
            List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, page);
            return PageResult.<SysUser>builder()
                    .total(count)
                    .data(list)
                    .build();
        }

        return PageResult.<SysUser>builder().build();
    }

    /**
     * 获取所有用户
     * @return
     */
    public List<SysUser> getAll() {
        return sysUserMapper.getAll();
    }

    /**
     * 检查邮箱是否已存在
     * @param mail
     * @param userId
     * @return
     */
    private boolean checkEmailExist(String mail, Integer userId) {
        return sysUserMapper.countByMail(mail, userId) > 0;
    }

    /**
     * 检查电话是否已存在
     * @param telephone
     * @param userId
     * @return
     */
    private boolean checkTelephoneExist(String telephone, Integer userId) {
        return sysUserMapper.countByTelephone(telephone, userId) > 0;
    }
}
