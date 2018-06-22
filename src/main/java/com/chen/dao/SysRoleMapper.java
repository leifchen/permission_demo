package com.chen.dao;

import com.chen.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper
 * @Author LeifChen
 * @Date 2018-06-04
 */
public interface SysRoleMapper {

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
    int insert(SysRole record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(SysRole record);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SysRole selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysRole record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysRole record);

    /**
     * 获取所有角色
     * @return
     */
    List<SysRole> getAllRole();

    /**
     * 根据name查询角色记录数
     * @param name
     * @param id
     * @return
     */
    int countByName(@Param("name")String name,@Param("id") Integer id);

    /**
     * 根据id列表查询角色列表
     * @param idList
     * @return
     */
    List<SysRole> getByIdList(@Param("idList") List<Integer> idList);
}