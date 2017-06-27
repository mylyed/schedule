package io.github.mylyed.schedule.dao;

import io.github.mylyed.schedule.entity.ScheduleJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lilei on 2017/6/22.
 */
public interface ScheduleJobRepository extends JpaRepository<ScheduleJobEntity, Long> {
}
