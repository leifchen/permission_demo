package com.chen.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * LogVO
 * @Author LeifChen
 * @Date 2018-06-12
 */
@Getter
@Setter
@ToString
public class LogVO {

    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private String fromTime;

    private String toTime;
}
