package com.chen.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 日志DTO
 * @Author LeifChen
 * @Date 2018-06-12
 */
@Getter
@Setter
@ToString
public class LogDTO {

    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;

    private Date fromTime;

    private Date toTime;
}
