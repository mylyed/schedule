package io.github.mylyed.schedule.config;

import io.github.mylyed.schedule.interceptor.GlobalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 重庆渝欧跨境电子商务有限公司
 * ========================
 * Author: lilei
 * Date: 17/6/20
 * Time: 14:55
 * Describe: 拦截器配置
 */
@Configuration
@Slf4j
public class InterceptorConfigurer extends WebMvcConfigurerAdapter {
    @Autowired
    private GlobalInterceptor globalInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

}