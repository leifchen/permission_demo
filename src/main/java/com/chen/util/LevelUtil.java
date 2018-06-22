package com.chen.util;

import org.apache.commons.lang3.StringUtils;

/**
 * LevelUtil
 * @Author LeifChen
 * @Date 2018-04-18
 */
public class LevelUtil {

    /**
     * 分隔符
     */
    public final static String SEPARATOR = ".";

    /**
     * 根节点
     */
    public final static String ROOT = "0";

    /**
     * 计算层级
     * @param parentLevel 父层级
     * @param parentId    父层级id
     * @return 例如 0, 0.1, 0.1.1
     */
    public static String calculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }
}
