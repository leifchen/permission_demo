package com.chen.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * StringUtil
 * @Author LeifChen
 * @Date 2018-06-07
 */
public class StringUtil {

    /**
     * 拆分字符串为List
     * @param str
     * @return
     */
    public static List<Integer> splitToListInt(String str) {
        List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
        return strList.stream().map(Integer::parseInt).collect(Collectors.toList());
    }
}
