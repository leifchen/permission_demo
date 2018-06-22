package com.chen.dao;

import com.chen.model.SysPermissionModule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限模块Mapper
 * @Author LeifChen
 * @Date 2018-06-04
 */
@Repository
public interface SysPermissionModuleMapper {

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
    int insert(SysPermissionModule record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(SysPermissionModule record);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SysPermissionModule selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysPermissionModule record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysPermissionModule record);

    /**
     * 通过父层级、名称、id查询记录条数
     * @param parentId
     * @param name
     * @param id
     * @return
     */
    int countByNameAndParentId(@Param("parentId") Integer parentId,
                               @Param("name") String name,
                               @Param("id") Integer id);

    /**
     * 获取所有
     * @return 权限模块列表
     */
    List<SysPermissionModule> getAllPermissionModule();

    /**
     * 通过层级获取子列表
     * @param level
     * @return
     */
    List<SysPermissionModule> getChildPermissionModuleListByLevel(@Param("level") String level);

    /**
     * 批量更新层级列表
     * @param sysPermissionModuleList
     */
    void batchUpdateLevel(@Param("sysPermissionModuleList") List<SysPermissionModule> sysPermissionModuleList);

    /**
     * 通过父级权限模块id查询记录数
     * @param parentId 父级权限模块id
     * @return
     */
    int countByParentId(@Param("parentId") int parentId);
}