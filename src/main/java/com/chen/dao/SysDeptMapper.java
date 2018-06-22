package com.chen.dao;

import com.chen.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门Mapper
 * @Author LeifChen
 * @Date 2018-06-04
 */
public interface SysDeptMapper {

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
    int insert(SysDept record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(SysDept record);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SysDept selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysDept record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysDept record);

    /**
     * 通过父层级、名称、id查询记录条数
     * @param parentId 父层级
     * @param name     部门名称
     * @param id       部门id
     * @return 记录条数
     */
    int countByNameAndParentId(@Param("parentId") Integer parentId,
                               @Param("name") String name,
                               @Param("id") Integer id);

    /**
     * 获取所有部门
     * @return 部门列表
     */
    List<SysDept> getAllDept();

    /**
     * 通过层级获取子部门列表
     * @param level 部门层级
     * @return 子部门列表
     */
    List<SysDept> getChildDeptListByLevel(@Param("level") String level);

    /**
     * 批量更新层级列表
     * @param sysDeptList 部门列表
     */
    void batchUpdateLevel(@Param("sysDeptList") List<SysDept> sysDeptList);

    /**
     * 根据父级部门id查询记录数
     * @param parentId 父级部门id
     * @return
     */
    int countByParentId(@Param("parentId") int parentId);
}