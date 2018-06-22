package com.chen.dto;

import com.chen.model.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 部门层级DTO
 * @Author LeifChen
 * @Date 2018-04-18
 */
@Getter
@Setter
@ToString
public class DeptLevelDTO extends SysDept {

    private List<DeptLevelDTO> deptList;

    public static DeptLevelDTO adapt(SysDept dept) {
        DeptLevelDTO dto = new DeptLevelDTO();
        BeanUtils.copyProperties(dept, dto);
        return dto;
    }
}
