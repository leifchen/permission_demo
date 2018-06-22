package com.chen.filter;

import com.chen.common.ApplicationContextHelper;
import com.chen.common.JsonData;
import com.chen.common.RequestHolder;
import com.chen.model.SysUser;
import com.chen.service.SysCoreService;
import com.chen.util.JsonMapperUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 权限Filter
 * @Author LeifChen
 * @Date 2018-06-11
 */
@Slf4j
public class PermissionFilter implements Filter {

    private static Set<String> exclusionUrlSet = new ConcurrentSkipListSet<>();

    private final static String NO_AUTH_URL = "/sys/user/noAuth.page";

    private final static String URL_JSON = ".json";

    @Override
    public void init(FilterConfig filterConfig) {
        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlList);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Map requestMap = httpRequest.getParameterMap();
        String servletPath = httpRequest.getServletPath();
        if (exclusionUrlSet.contains(servletPath)) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        // 用户是否登录
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser == null) {
            log.info("visit {}, no login, parameter:{}", servletPath, JsonMapperUtil.obj2String(requestMap));
            noAuth(httpRequest, httpResponse);
            return;
        }

        // 用户是否拥有对应url的访问权限
        SysCoreService sysCoreService = ApplicationContextHelper.popBean(SysCoreService.class);
        if (sysCoreService != null && !sysCoreService.hasUrlPermission(servletPath)) {
            log.info("{} visit {}, no permission, parameter:{}", sysUser.getUsername(), servletPath, JsonMapperUtil.obj2String(requestMap));
            noAuth(httpRequest, httpResponse);
            return;
        }

        chain.doFilter(httpRequest, httpResponse);
    }

    @Override
    public void destroy() {

    }

    /**
     * 无权限访问
     * @param request
     * @param response
     */
    private void noAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String servletPath = request.getServletPath();
        if (servletPath.endsWith(URL_JSON)) {
            JsonData jsonData = JsonData.fail("没有访问权限，如需要访问，请联系管理员");
            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(JsonMapperUtil.obj2String(jsonData));
        } else {
            clientRedirect(response, NO_AUTH_URL);
        }
    }

    private void clientRedirect(HttpServletResponse response, String url) throws IOException {
        response.setHeader("Content-Type", "text/html");
        response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                + "window.location.href='" + url + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");
    }
}
