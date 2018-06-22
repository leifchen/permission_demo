package com.chen.dao;

import com.chen.bean.PageQuery;
import com.chen.model.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限点Mapper
 * @Author LeifChen
 * @Date 2018-06-04
 */
public interface SysPermissionMapper {

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
    int insert(SysPermission record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(SysPermission record);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SysPermission selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysPermission record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysPermission record);

    /**
     * 根据权限模块id、name查询
     * @param permissionModuleId
     * @param name
     * @param id
     * @return
     */
    int countByNameAndPermissionModuleId(@Param("permissionModuleId") Integer permissionModuleId,
                                         @Param("name") String name,
                                         @Param("id") Integer id);

    /**
     * 通过权限模块id查询记录条数
     * @param permissionModuleId
     * @return
     */
    int countByPermissionModuleId(@Param("permissionModuleId") Integer permissionModuleId);

    /**
     * 根据权限模块id、分页参数查询权限点列表
     * @param permissionModuleId
     * @param page
     * @return
     */
    List<SysPermission> getPageByPermissionModuleId(@Param("permissionModuleId") Integer permissionModuleId, @Param("page") PageQuery page);

    /**
     * 获取所有权限点
     * @return
     */
    List<SysPermission> getAll();

    /**
     * 根据id列表查询权限点列表
     * @param idList
     * @return
     */
    List<SysPermission> getByIdList(@Param("idList") List<Integer> idList);

    /**
     * 根据url查询权限点列表
     * @param url
     * @return
     */
    List<SysPermission> getByUrl(@Param("url") String url);
}