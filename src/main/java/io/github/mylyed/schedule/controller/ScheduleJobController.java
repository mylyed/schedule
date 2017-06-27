package io.github.mylyed.schedule.controller;

import io.github.mylyed.schedule.dao.ScheduleJobRepository;
import io.github.mylyed.schedule.dto.DataTablesPage;
import io.github.mylyed.schedule.dto.PageArgs;
import io.github.mylyed.schedule.dto.Result;
import io.github.mylyed.schedule.entity.ScheduleJobEntity;
import io.github.mylyed.schedule.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * Created by lilei on 2017/6/22.
 */
@Controller
@RequestMapping("schedule")
@Slf4j
public class ScheduleJobController {
    @Autowired
    ScheduleJobRepository scheduleJobRepository;

    @Autowired
    ScheduleService scheduleService;

    @GetMapping({"", "index", "/"})
    public String index() {
        return "schedule";
    }


    @RequestMapping("/jobs")
    @ResponseBody
    public Object jobs(PageArgs pageArgs) {

        Pageable pageable = new PageRequest(pageArgs.getStart() / pageArgs.getLength(),
                pageArgs.getLength());


        log.info("分页参数{}", pageable);
        DataTablesPage<ScheduleJobEntity> datatablesPage = new DataTablesPage<>(scheduleJobRepository.findAll(pageable), pageArgs.getDraw());
        return datatablesPage;
    }


    /**
     * 新增或者修改
     *
     * @param scheduleJobEntity
     * @return
     * @throws SchedulerException
     */
    @PostMapping("/save")
    @ResponseBody
    public Object saveJob(@RequestBody ScheduleJobEntity scheduleJobEntity) throws SchedulerException {
        scheduleService.saveJob(scheduleJobEntity);
        return Result.success();
    }


    /**
     * 继续任务
     *
     * @param scheduleJobEntitys
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Object deleteJob(@RequestBody List<ScheduleJobEntity> scheduleJobEntitys) {
        scheduleJobEntitys.stream().map(ScheduleJobEntity::getJobId).filter(Objects::nonNull)
                .forEach(d -> scheduleService.deleteJob(d));
        return Result.success();
    }

    /**
     * 继续任务
     *
     * @param scheduleJobEntitys
     * @return
     */
    @PostMapping("/resume")
    @ResponseBody
    public Object resumeJob(@RequestBody List<ScheduleJobEntity> scheduleJobEntitys) {
        scheduleJobEntitys.stream().filter(d -> d.getStatus() == ScheduleJobEntity.ScheduleStatus.PAUSE).map(ScheduleJobEntity::getJobId).filter(Objects::nonNull)
                .forEach(d -> scheduleService.resumeJob(d));
        return Result.success();
    }

    /**
     * 暂停任务
     *
     * @param scheduleJobEntitys
     * @return
     */
    @PostMapping("/pause")
    @ResponseBody
    public Object pauseJob(@RequestBody List<ScheduleJobEntity> scheduleJobEntitys) {
        scheduleJobEntitys.stream().filter(d -> d.getStatus() == ScheduleJobEntity.ScheduleStatus.NORMAL).map(ScheduleJobEntity::getJobId).filter(Objects::nonNull)
                .forEach(d -> scheduleService.pauseJob(d));
        return Result.success();
    }

    /**
     * 触发任务
     *
     * @param scheduleJobEntitys
     * @return
     */
    @PostMapping("/trigger")
    @ResponseBody
    public Object triggerJob(@RequestBody ScheduleJobEntity scheduleJobEntitys) {
        scheduleService.triggerJob(scheduleJobEntitys.getJobId());
        return Result.success();
    }


}
