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
 * 权限点VO
 * @Author LeifChen
 * @Date 2018-06-05
 */
@Getter
@Setter
@ToString
public class PermissionVO {

    private Integer id;

    @NotBlank(message = "权限点名称不能为空")
    @Length(min = 1, max = 20, message = "用户名长度需要在20个字以内")
    private String name;

    @NotNull(message = "必须指定权限模块")
    private Integer permissionModuleId;

    @NotBlank(message = "权限点的请求url不能为空")
    @Length(min = 6, max = 100, message = "请求url的长度需要在6~100个字符之间")
    private String url;

    @NotNull(message = "必须指定权限点类型")
    @Min(value = 1, message = "权限点类型不合法")
    @Max(value = 3, message = "权限点类型不合法")
    private Integer type;

    @NotNull(message = "必须指定权限点状态")
    @Min(value = 0, message = "权限点状态不合法")
    @Max(value = 2, message = "权限点状态不合法")
    private Integer status;

    @NotNull(message = "权限点的展示顺序不能为空")
    private Integer seq;

    @Length(max = 200, message = "备注长度需要在200个字以内")
    private String remark;
}
