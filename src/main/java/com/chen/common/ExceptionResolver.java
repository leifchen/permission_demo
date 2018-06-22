package com.chen.common;

import com.chen.exception.PermissionException;
import com.chen.exception.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理类
 * @Author LeifChen
 * @Date 2018-04-11
 */
@Component("exceptionResolver")
@Slf4j
public class ExceptionResolver implements HandlerExceptionResolver {

    private final static String URL_JSON = ".json";
    private final static String URL_PAGE = ".page";

    /**
     * 异常处理
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     */
    @Nullable
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
        String url = request.getRequestURL().toString();
        ModelAndView mv;
        String defaultMsg = "System error";

        // 请求json数据，使用.json结尾
        if (url.endsWith(URL_JSON)) {
            if (ex instanceof PermissionException || ex instanceof ValidateException) {
                JsonData result = JsonData.fail(ex.getMessage());
                mv = new ModelAndView(new MappingJackson2JsonView(), result.toMap());
            } else {
                log.error("unknown json exception, url:" + url, ex);
                JsonData result = JsonData.fail(defaultMsg);
                mv = new ModelAndView(new MappingJackson2JsonView(), result.toMap());
            }
        } else if (url.endsWith(URL_PAGE)) {
            // 请求page页面，使用.page结尾
            log.error("unknown page exception, url:" + url, ex);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("exception", result.toMap());
        } else {
            log.error("unknown exception, url:" + url, ex);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView(new MappingJackson2JsonView(), result.toMap());
        }

        return mv;
    }
}
