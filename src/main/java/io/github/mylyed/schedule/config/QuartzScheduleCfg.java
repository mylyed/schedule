package io.github.mylyed.schedule.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

/**
 * Created by lilei on 2017/6/23.
 */
@Configuration
public class QuartzScheduleCfg {

    @Bean
    public SchedulerFactoryBean scheduler(@Qualifier("dataSource") @Autowired DataSource dataSource) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setSchedulerName("调度器");
        //        延时启动
        schedulerFactoryBean.setStartupDelay(10);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setAutoStartup(true);
        return schedulerFactoryBean;
    }

}
