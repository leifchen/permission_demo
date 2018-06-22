package com.chen.dao;

import com.chen.bean.PageQuery;
import com.chen.dto.LogDTO;
import com.chen.model.SysLog;
import com.chen.model.SysLogWithBlobs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 日志Mapper
 * @Author LeifChen
 * @Date 2018-06-04
 */
public interface SysLogMapper {

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
    int insert(SysLogWithBlobs record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(SysLogWithBlobs record);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SysLogWithBlobs selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysLogWithBlobs record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKeyWithBLOBs(SysLogWithBlobs record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysLog record);

    /**
     * 通过搜索条件查询日志记录
     * @param dto
     * @return
     */
    int countBySearch(@Param("dto") LogDTO dto);

    /**
     * 分页查询日志记录列表
     * @param dto
     * @param page
     * @return
     */
    List<SysLogWithBlobs> getPageListBySearch(@Param("dto") LogDTO dto, @Param("page") PageQuery page);
}