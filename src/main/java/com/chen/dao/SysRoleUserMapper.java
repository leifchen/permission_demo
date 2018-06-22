package com.chen.dao;

import com.chen.model.SysRoleUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色Mapper
 * @Author LeifChen
 * @Date 2018-06-04
 */
public interface SysRoleUserMapper {

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
    int insert(SysRoleUser record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(SysRoleUser record);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SysRoleUser selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysRoleUser record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysRoleUser record);

    /**
     * 根据用户id查询角色id列表
     * @param userId 用户id
     * @return
     */
    List<Integer> getRoleIdListByUserId(@Param("userId") int userId);

    /**
     * 根据角色id查询用户id列表
     * @param roleId 角色id
     * @return
     */
    List<Integer> getUserIdListByRoleId(@Param("roleId") int roleId);

    /**
     * 根据角色id删除用户
     * @param roleId 角色id
     */
    void deleteByRoleId(@Param("roleId") int roleId);

    /**
     * 批量新增角色-用户关系
     * @param roleUserList 角色-用户列表
     */
    void batchInsert(@Param("roleUserList") List<SysRoleUser> roleUserList);

    /**
     * 根据角色id列表查询用户id列表
     * @param roleIdList 角色id列表
     * @return
     */
    List<Integer> getUseIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);
}