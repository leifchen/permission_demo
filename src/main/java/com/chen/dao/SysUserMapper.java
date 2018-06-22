package com.chen.dao;

import com.chen.bean.PageQuery;
import com.chen.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper
 * @Author LeifChen
 * @Date 2018-06-04
 */
public interface SysUserMapper {

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增
     * @param record
     * @return
     */
    int insert(SysUser record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(SysUser record);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SysUser selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysUser record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysUser record);

    /**
     * 根据关键字查询用户
     * @param keyword
     * @return
     */
    SysUser findByKeyword(@Param("keyword") String keyword);

    /**
     * 通过邮箱、id查询记录条数
     * @param mail
     * @param id
     * @return
     */
    int countByMail(@Param("mail") String mail, @Param("id") Integer id);

    /**
     * 通过电话、id查询记录条数
     * @param telephone
     * @param id
     * @return
     */
    int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer id);

    /**
     * 通过所属部门查询记录条数
     * @param deptId 部门id
     * @return
     */
    int countByDeptId(@Param("deptId") int deptId);

    /**
     * 根据所属部门id、分页参数查询用户列表
     * @param deptId 部门id
     * @param page 分页参数
     * @return
     */
    List<SysUser> getPageByDeptId(@Param("deptId") int deptId, @Param("page") PageQuery page);

    /**
     * 根据用户id列表查询用户
     * @param idList 用户id列表
     * @return
     */
    List<SysUser> getByIdList(@Param("idList") List<Integer> idList);

    /**
     * 获取所有用户
     * @return
     */
    List<SysUser> getAll();
}