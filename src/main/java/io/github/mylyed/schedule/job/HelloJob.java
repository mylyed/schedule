package io.github.mylyed.schedule.job;

import io.github.mylyed.schedule.dao.ScheduleJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lilei on 2017/6/24.
 */
@Job
@Slf4j
public class HelloJob {

    public HelloJob() {
        log.info("HelloJob 初始化");
    }


    @Autowired
    ScheduleJobRepository scheduleJobRepository;

    public void hello() {
        log.info("没有参数的方法");
        Long count = scheduleJobRepository.count();
        log.info("任务个数{}", count);
    }

    public void hello(String millis) {
        log.info("任务开始");
        log.info("1个参数的方法");
        Long count = scheduleJobRepository.count();
        try {
            Thread.sleep(Long.valueOf(millis));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("任务个数{}", count);
        log.info("任务结束");
    }

    public void hello(String millis, String msg) {
        log.info("2个参数的方法");
        log.info("msg->{}", msg);
        Long count = scheduleJobRepository.count();
        try {
            Thread.sleep(Long.valueOf(millis));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("任务个数{}", count);
    }


}
