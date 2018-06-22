package com.chen.dto;

import com.chen.model.SysPermissionModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 权限模块层级DTO
 * @Author LeifChen
 * @Date 2018-06-01
 */
@Getter
@Setter
@ToString
public class PermissionModuleLevelDTO extends SysPermissionModule {

    private List<PermissionModuleLevelDTO> permissionModuleList;

    private List<PermissionDTO> permissionDTOList;

    public static PermissionModuleLevelDTO adapt(SysPermissionModule permissionModule) {
        PermissionModuleLevelDTO dto = new PermissionModuleLevelDTO();
        BeanUtils.copyProperties(permissionModule, dto);
        return dto;
    }
}
