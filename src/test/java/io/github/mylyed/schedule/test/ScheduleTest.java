package io.github.mylyed.schedule.test;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by lilei on 2017/6/22.
 */

@Slf4j
public class ScheduleTest {

    public static void main(String[] args) throws SchedulerException {


        //通过schedulerFactory获取一个调度器
        SchedulerFactory schedulerfactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerfactory.getScheduler();

        scheduler.start();

        JobDetail job = JobBuilder.newJob(MyJob.class).withIdentity("job1", "group1").build();
        job.getJobDataMap().put("aa", "msg");

        job.getJobDataMap().put("aa", "msg");


        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ? ").withMisfireHandlingInstructionDoNothing();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("job1", "group1").withSchedule(scheduleBuilder).build();


        scheduler.scheduleJob(job, trigger);


    }

    @Slf4j
    public static class MyJob implements org.quartz.Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            log.info("附加数据{}", new Gson().toJson(context.getJobDetail().getJobDataMap()));
            log.info("Hello World!  MyJob is executing.");
        }
    }

}
