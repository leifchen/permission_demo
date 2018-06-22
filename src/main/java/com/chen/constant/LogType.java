package com.chen.constant;

/**
 * 日志类型
 * @Author LeifChen
 * @Date 2018-06-12
 */
public interface LogType {

    /**
     * 部门
     */
    int TYPE_DEPT = 1;

    /**
     * 用户
     */
    int TYPE_USER = 2;

    /**
     * 权限模块
     */
    int TYPE_PERMISSION_MODULE = 3;

    /**
     * 权限点
     */
    int TYPE_PERMISSION = 4;

    /**
     * 角色
     */
    int TYPE_ROLE = 5;

    /**
     * 角色权限关系
     */
    int TYPE_ROLE_PERMISSION = 6;

    /**
     * 角色用户关系
     */
    int TYPE_ROLE_USER = 7;
}
