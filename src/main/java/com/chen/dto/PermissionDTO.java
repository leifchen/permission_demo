package com.chen.dto;

import com.chen.model.SysPermission;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * 权限DTO
 * @Author LeifChen
 * @Date 2018-06-05
 */
@Getter
@Setter
@ToString
public class PermissionDTO extends SysPermission{

    /**
     * 是否默认选中
     */
    private Boolean checked;

    /**
     * 是否有权限操作
     */
    private Boolean hasPermission;

    public static PermissionDTO adapt(SysPermission permission) {
        PermissionDTO dto = new PermissionDTO();
        BeanUtils.copyProperties(permission, dto);
        dto.setChecked(false);
        dto.setHasPermission(false);
        return dto;
    }
}
