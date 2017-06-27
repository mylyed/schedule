package io.github.mylyed.schedule.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jca.context.SpringContextResourceAdapter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Spring implements ApplicationContextAware {

    private static ApplicationContext ctx = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
        log.info("已注册ApplicationContext");
    }

    public static ApplicationContext context() {
        return ctx;
    }


}  