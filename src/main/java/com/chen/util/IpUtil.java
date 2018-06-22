package com.chen.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IpUtil
 * @Author LeifChen
 * @Date 2018-05-31
 */
@Slf4j
public class IpUtil {

    private final static String ERROR_IP = "127.0.0.1";
    private final static String LOCALHOST_IP = "0:0:0:0:0:0:0:1";
    private final static String UNKNOWN = "unknown";

    public final static Pattern PATTERN = Pattern.compile("(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})");

    /**
     * 获取用户的真实ip
     * @param request
     * @return
     */
    public static String getUserIP(HttpServletRequest request) {
        // 优先取X-Real-IP
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (LOCALHOST_IP.equals(ip)) {
                ip = ERROR_IP;
            }
        }

        if (UNKNOWN.equalsIgnoreCase(ip)) {
            ip = ERROR_IP;
            return ip;
        }

        int pos = ip.indexOf(',');
        if (pos >= 0) {
            ip = ip.substring(0, pos);
        }

        return ip;
    }

    public static String getLastIpSegment(HttpServletRequest request) {
        String ip = getUserIP(request);
        if (ip != null) {
            ip = ip.substring(ip.lastIndexOf('.') + 1);
        } else {
            ip = "0";
        }
        return ip;
    }

    /**
     * 判断ip是否是一个符合规则ip
     * @param request
     * @return
     */
    public static boolean isValidIP(HttpServletRequest request) {
        String ip = getUserIP(request);
        return isValidIP(ip);
    }

    /**
     * 判断ip是否是一个符合规则ip
     * @param ip
     * @return
     */
    public static boolean isValidIP(String ip) {
        if (StringUtils.isEmpty(ip)) {
            log.debug("ip is null. valid result is false");
            return false;
        }

        Matcher matcher = PATTERN.matcher(ip);
        boolean isValid = matcher.matches();
        log.debug("valid ip:" + ip + " result is: " + isValid);
        return isValid;
    }

    public static String getLastServerIpSegment() {
        String ip = getServerIP();
        if (ip != null) {
            ip = ip.substring(ip.lastIndexOf('.') + 1);
        } else {
            ip = "0";
        }
        return ip;
    }

    public static String getServerIP() {
        InetAddress inet;
        try {
            inet = InetAddress.getLocalHost();
            return inet.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }
}