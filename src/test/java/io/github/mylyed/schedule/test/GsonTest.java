package io.github.mylyed.schedule.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.mylyed.schedule.entity.ScheduleJobEntity;

import java.text.SimpleDateFormat;

/**
 * Created by lilei on 2017/6/25.
 */
public class GsonTest {

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().create();
        ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
        scheduleJobEntity.prePersist();
        String s
                = gson.toJson(scheduleJobEntity);
        System.out.println(s);
        ScheduleJobEntity scheduleJobEntity1 = gson.fromJson(s, ScheduleJobEntity.class);

        System.out.println(scheduleJobEntity1.toString());
    }
}
