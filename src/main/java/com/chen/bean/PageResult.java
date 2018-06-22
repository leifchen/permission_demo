package com.chen.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 分页结构Bean
 * @Author LeifChen
 * @Date 2018-05-31
 */
@Getter
@Setter
@ToString
@Builder
public class PageResult<T> {

    private List<T> data;

    private int total;
}
