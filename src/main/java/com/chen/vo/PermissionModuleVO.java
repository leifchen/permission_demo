package com.chen.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 权限模块VO
 * @Author LeifChen
 * @Date 2018-05-31
 */
@Getter
@Setter
@ToString
public class PermissionModuleVO {

    private Integer id;

    @NotBlank(message = "权限模块的名称不能为空")
    @Length(min = 2, max = 20, message = "权限模块名称需在2~20个字之间")
    private String name;

    private Integer parentId;

    @NotNull(message = "权限模块的展示顺序不能为空")
    private Integer seq;

    @NotNull(message = "权限模块的状态不能为空")
    @Min(value = 0, message = "权限模块的状态不合法")
    @Max(value = 1, message = "权限模块的状态不合法")
    private Integer status;

    @Length(max = 200, message = "权限模块的备注需在200个字以内")
    private String remark;
}
