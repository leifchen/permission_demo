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
 * RoleVO
 * @Author LeifChen
 * @Date 2018-06-05
 */
@Getter
@Setter
@ToString
public class RoleVO {

    private Integer id;

    @NotBlank(message = "角色名称不能为空")
    @Length(min = 2, max = 20, message = "角色名称长度需在2~20个字以内")
    private String name;

    @NotNull(message = "角色类型不能为空")
    @Min(value = 1, message = "角色类型不合法")
    @Max(value = 2, message = "角色类型不合法")
    private Integer type;

    @NotNull(message = "角色状态不能为空")
    @Min(value = 0, message = "角色状态不合法")
    @Max(value = 1, message = "角色状态不合法")
    private Integer status;

    @Length(max = 200, message = "角色备注的长度需在200字以内")
    private String remark;
}
