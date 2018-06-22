package com.chen.dao;

import com.chen.model.SysRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色权限Mapper
 * @Author LeifChen
 * @Date 2018-06-04
 */
public interface SysRolePermissionMapper {

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
    int insert(SysRolePermission record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(SysRolePermission record);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SysRolePermission selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysRolePermission record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysRolePermission record);

    /**
     * 根据角色id列表查询权限点id列表
     * @param roleIdList
     * @return
     */
    List<Integer> getPermissionIdListByRoleIdList(@Param("roleIdList")List<Integer> roleIdList);

    /**
     * 根据角色id删除权限
     * @param roleId 角色id
     */
    void deleteByRoleId(@Param("roleId") int roleId);

    /**
     * 批量新增角色-权限
     * @param rolePermissionList
     */
    void batchInsert(@Param("rolePermissionList") List<SysRolePermission> rolePermissionList);

    /**
     * 根据权限点id查询角色id列表
     * @param permissionId 权限点id
     * @return
     */
    List<Integer> getRoleIdListByPermissionId(@Param("permissionId") int permissionId);
}