package io.github.mylyed.schedule.service;

import io.github.mylyed.schedule.core.ScheduleJob;
import io.github.mylyed.schedule.dao.ScheduleJobRepository;
import io.github.mylyed.schedule.entity.ScheduleJobEntity;
import io.github.mylyed.schedule.exception.ScheduleJobException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lilei on 2017/6/24.
 */
@Service
@Slf4j
public class ScheduleService {
    @Autowired
    ScheduleJobRepository scheduleJobRepository;

    @Autowired
    Scheduler scheduler;

    /**
     * 保存或者修改任务
     *
     * @param scheduleJobEntity
     * @throws SchedulerException
     */
    public void saveJob(ScheduleJobEntity scheduleJobEntity) {
        boolean add = (scheduleJobEntity.getJobId() == null);
        try {
            scheduleJobRepository.save(scheduleJobEntity);

            TriggerKey triggerKey = triggerKey(scheduleJobEntity);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJobEntity.getCronExpression()).withMisfireHandlingInstructionDoNothing();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            if (add) {
                log.info("新增任务");
                JobKey jobKey = jobKey(scheduleJobEntity);
                JobDetail job = JobBuilder.newJob(ScheduleJob.class).withIdentity(jobKey).withDescription(scheduleJobEntity.getRemark()).build();
                job.getJobDataMap().put(ScheduleJob.JOB_ID_KEY, scheduleJobEntity.getJobId());
                scheduler.scheduleJob(job, trigger);
            } else {
                log.info("修改任务");
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            String msg = (add ? "新增" : "保存") + "任务失败";
            log.error(msg, e);
            throw new ScheduleJobException(msg);
        }
    }

    /**
     * 暂停任务
     *
     * @param jobId
     * @throws SchedulerException
     */
    public void pauseJob(Long jobId) {
        try {
            ScheduleJobEntity scheduleJobEntity = scheduleJobRepository.findOne(jobId);
            scheduleJobEntity.setStatus(ScheduleJobEntity.ScheduleStatus.PAUSE);
            scheduleJobRepository.saveAndFlush(scheduleJobEntity);
            JobKey jobKey = jobKey(scheduleJobEntity);
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            log.error("暂停任务失败", e);
            throw new ScheduleJobException("暂停任务失败");
        }
    }

    /**
     * 恢复任务
     *
     * @param jobId
     * @throws SchedulerException
     */
    public void resumeJob(Long jobId) {
        try {
            ScheduleJobEntity scheduleJobEntity = scheduleJobRepository.findOne(jobId);
            scheduleJobEntity.setStatus(ScheduleJobEntity.ScheduleStatus.NORMAL);
            scheduleJobRepository.saveAndFlush(scheduleJobEntity);
            JobKey jobKey = jobKey(scheduleJobEntity);
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            log.error("恢复任务失败", e);
            throw new ScheduleJobException("恢复任务失败");
        }
    }

    /**
     * 删除任务
     *
     * @param jobId
     * @throws SchedulerException
     */
    public void deleteJob(Long jobId) {
        try {
            scheduleJobRepository.delete(jobId);
            ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
            scheduleJobEntity.setJobId(jobId);

            JobKey jobKey = jobKey(scheduleJobEntity);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            log.error("删除任务失败", e);
            throw new ScheduleJobException("删除任务失败");
        }
    }

    /**
     * 立即触发任务
     *
     * @param jobId
     * @throws SchedulerException
     */
    public void triggerJob(Long jobId) {
        try {
            ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
            scheduleJobEntity.setJobId(jobId);
            JobKey jobKey = jobKey(scheduleJobEntity);
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            log.error("触发任务失败", e);
            throw new ScheduleJobException("触发任务失败");
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private static final String JOB_PREFIX = "JOB_";
    private static final String TRIGGER_PREFIX = "TRIGGER_";


    private JobKey jobKey(ScheduleJobEntity scheduleJobEntity) {
        return JobKey.jobKey(JOB_PREFIX + scheduleJobEntity.getJobId());
    }

    private TriggerKey triggerKey(ScheduleJobEntity scheduleJobEntity) {
        return TriggerKey.triggerKey(TRIGGER_PREFIX + scheduleJobEntity.getJobId());
    }

}
