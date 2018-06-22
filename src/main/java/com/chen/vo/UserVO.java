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
 * 用户VO
 * @Author LeifChen
 * @Date 2018-05-29
 */
@Getter
@Setter
@ToString
public class UserVO {

    private Integer id;

    @NotBlank(message = "用户名不能为空")
    @Length(min = 1, max = 20, message = "用户名长度需要在20个字以内")
    private String username;

    @NotBlank(message = "电话不能为空")
    @Length(min = 1, max = 13, message = "电话长度需要在13个字以内")
    private String telephone;

    @NotBlank(message = "邮箱不能为空")
    @Length(min = 5, max = 50, message = "邮箱长度需要在50个字以内")
    private String mail;

    @NotNull(message = "必须指定用户所属部门")
    private Integer deptId;

    @NotNull(message = "必须指定用户的状态")
    @Min(value = 0, message = "用户状态不合法")
    @Max(value = 2, message = "用户状态不合法")
    private Integer status;

    @Length(max = 200, message = "备注长度需要在200个字以内")
    private String remark;
}
