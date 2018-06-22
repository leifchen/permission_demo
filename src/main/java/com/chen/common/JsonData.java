package com.chen.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Json请求对象
 * @Author LeifChen
 * @Date 2018-04-11
 */
@Getter
@Setter
public class JsonData {

    /**
     * Json请求标识（true：成功；false：失败）
     */
    private boolean ret;

    /**
     * Json请求返回信息
     */
    private String msg;

    /**
     * Json请求返回数据
     */
    private Object data;

    public JsonData(boolean ret) {
        this.ret = ret;
    }

    /**
     * Json请求成功
     * @param data 返回数据
     * @param msg  返回信息
     * @return
     */
    public static JsonData success(Object data, String msg) {
        JsonData jsonData = new JsonData(true);
        jsonData.msg = msg;
        jsonData.data = data;
        return jsonData;
    }

    /**
     * Json请求成功
     * @param data 返回信息
     * @return
     */
    public static JsonData success(Object data) {
        JsonData jsonData = new JsonData(true);
        jsonData.data = data;
        return jsonData;
    }

    /**
     * Json请求成功
     * @return
     */
    public static JsonData success() {
        return new JsonData(true);
    }

    /**
     * Json请求失败
     * @param msg 返回信息
     * @return
     */
    public static JsonData fail(String msg) {
        JsonData jsonData = new JsonData(false);
        jsonData.msg = msg;
        return jsonData;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>(3);
        result.put("ret", ret);
        result.put("msg", msg);
        result.put("data", data);
        return result;
    }
}
