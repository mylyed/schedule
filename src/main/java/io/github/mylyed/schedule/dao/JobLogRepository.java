package io.github.mylyed.schedule.dao;

import io.github.mylyed.schedule.entity.JobLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lilei on 2017/6/22.
 */
public interface JobLogRepository extends JpaRepository<JobLogEntity, Long> {

    JobLogEntity findByJobId(Long jobId);
}
