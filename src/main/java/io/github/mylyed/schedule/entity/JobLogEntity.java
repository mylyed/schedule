package io.github.mylyed.schedule.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lilei on 2017/6/22.
 */

@Entity
@Table(name = "t_job_log")
@Data
public class JobLogEntity {

    @Id
    @GeneratedValue
    private Long logId;

    /**
     * 任务id
     */
    private Long jobId;


    /**
     * 日志信息
     */
    private String msg;

    /**
     * 运行参数
     */
    private String jobArgs;

    /**
     * 定时任务启动时间
     */

    private Date startTime;

    /**
     * 耗费时间单位秒
     */
    private Double spendTime;

    /**
     * 任务状态
     */
    private JobLogStatus status;


    private String jobJson;

    //    @PrePersist
    //    public void prePersist() {
    //
    //    }

    public enum JobLogStatus {
        SUCCESS,
        FAIL
    }

}
