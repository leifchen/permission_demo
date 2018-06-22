package com.chen;

import org.mybatis.generator.api.ShellRunner;

/**
 * 根据表自动生成model、dao、mapper
 * @Author LeifChen
 * @Date 2018-04-10
 */
public class Generator {

    public static void main(String[] args) {
        args = new String[]{"-configfile", "generator/mybatis-generator.xml", "-overwrite"};
        ShellRunner.main(args);
    }
}
