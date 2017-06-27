package io.github.mylyed.schedule.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 重庆渝欧跨境电子商务有限公司
 * ========================
 * Author: 李磊
 * Date: 16/10/27
 * Time: 下午2:46
 * Describe: 并发判断
 */
@Slf4j
@Component
public class GlobalInterceptor implements HandlerInterceptor {

    private Set<String> taskPools = ConcurrentHashMap.newKeySet();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        log.debug("preHandle");
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.debug("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.debug("afterCompletion");
    }

}
