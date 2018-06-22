package com.chen.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 部门VO
 * @Author LeifChen
 * @Date 2018-04-18
 */
@Getter
@Setter
@ToString
public class DeptVO {

    private Integer id;

    @NotBlank(message = "部门名称不能为空")
    @Length(max = 15,min = 2,message = "部门名称的长度需在2-15之间")
    private String name;

    private Integer parentId;

    @NotNull(message = "展示顺序不能为空")
    private Integer seq;

    @Length(max = 150,message = "备注的长度不能超出150字")
    private String remark;
}
