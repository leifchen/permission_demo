package com.chen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 主程序入口
 * @Author LeifChen
 * @Date 2018-05-31
 */
@SpringBootApplication
@MapperScan(basePackages = "com.chen.dao")
public class PermissionDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PermissionDemoApplication.class, args);
    }
}
