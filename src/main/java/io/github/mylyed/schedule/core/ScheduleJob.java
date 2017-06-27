package io.github.mylyed.schedule.core;

import com.google.gson.Gson;
import io.github.mylyed.schedule.dao.JobLogRepository;
import io.github.mylyed.schedule.dao.ScheduleJobRepository;
import io.github.mylyed.schedule.entity.JobLogEntity;
import io.github.mylyed.schedule.entity.ScheduleJobEntity;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.Date;

@Slf4j
public class ScheduleJob implements org.quartz.Job {

    /**
     * 任务调度参数key
     */
    public static final String JOB_ID_KEY = "JOB_ID_KEY";


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("附加数据{}", new Gson().toJson(context.getJobDetail().getJobDataMap()));
        log.info("job 正在作业");

        Long jobId = context.getJobDetail().getJobDataMap().getLong(JOB_ID_KEY);

        ScheduleJobRepository scheduleJobRepository = Spring.context().getBean(ScheduleJobRepository.class);
        ScheduleJobEntity scheduleJobEntity = scheduleJobRepository.findOne(jobId);
        if (scheduleJobEntity == null) {
            log.warn("任务{}已经删除", jobId);
            return;
        }
        log.info(scheduleJobEntity.toString());

        JobLogEntity jobLogEntity = new JobLogEntity();
        jobLogEntity.setJobArgs(scheduleJobEntity.getParams());
        jobLogEntity.setJobId(scheduleJobEntity.getJobId());
        jobLogEntity.setJobJson(new Gson().toJson(scheduleJobEntity));
        try {
            Object bean = Spring.context().getBean(Class.forName(scheduleJobEntity.getClassName()));
            Method method;
            String params = scheduleJobEntity.getParams();
            jobLogEntity.setStartTime(new Date());
            final StopWatch stopWatch = new StopWatch();

            if (params == null || params.trim().length() == 0) {
                method = bean.getClass().getDeclaredMethod(scheduleJobEntity.getMethodName());
                stopWatch.start();
                method.invoke(bean);
            } else if (!params.contains(",")) {
                //一个参数
                method = bean.getClass().getDeclaredMethod(scheduleJobEntity.getMethodName(),
                        String.class);
                stopWatch.start();
                method.invoke(bean, params);
            } else {
                    //多个参数
                String[] strings = params.split(",");
                Class[] classes = new Class[strings.length];
                for (int i = 0; i < strings.length; i++) {
                    classes[i] = String.class;
                }
                method = bean.getClass().getDeclaredMethod(scheduleJobEntity.getMethodName(),
                        classes);
                stopWatch.start();
                method.invoke(bean, strings);
            }
            stopWatch.stop();
            jobLogEntity.setSpendTime(stopWatch.getTotalTimeSeconds());
            jobLogEntity.setStatus(JobLogEntity.JobLogStatus.SUCCESS);
        } catch (Exception e) {
            log.error("执行出错", e);
            jobLogEntity.setMsg(e.getClass().getName() + ",原因:" + e.getMessage());
            jobLogEntity.setStatus(JobLogEntity.JobLogStatus.FAIL);
        } finally {
            JobLogRepository jobLogRepository = Spring.context().getBean(JobLogRepository.class);
            jobLogRepository.save(jobLogEntity);
        }
    }
}