package io.github.mylyed.schedule.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lilei on 2017/6/22.
 */

@Entity
@Table(name = "t_schedule_job")
@Data
public class ScheduleJobEntity {

    /**
     * 任务id
     */
    @Id
    @GeneratedValue
    private Long jobId;

    /**
     * 类名称
     */
    @NotBlank(message = "类名称不能为空")
    private String className;

    /**
     * 方法名
     */
    @NotBlank(message = "方法名称不能为空")
    private String methodName;

    /**
     * 参数
     */
    private String params;

    /**
     * cron表达式
     */
    @NotBlank(message = "cron表达式不能为空")
    private String cronExpression;

    /**
     * 任务状态
     */
    private ScheduleStatus status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */

    private Date createTime;

    @PrePersist
    public void prePersist() {
        if (createTime == null) {
            setCreateTime(new Date());
        }
        if (status == null) {
            setStatus(ScheduleStatus.NORMAL);
        }
        if (className != null) {
            className = className.trim();
        }
        if (methodName != null) {
            methodName = methodName.trim();
        }
        if (cronExpression != null) {
            cronExpression = cronExpression.trim();
        }
        if (params != null) {
            params = params.trim();
        }

    }

    public enum ScheduleStatus implements Serializable {
        /**
         * 正常
         */
        NORMAL,
        /**
         * 暂停
         */
        PAUSE;
    }

}
