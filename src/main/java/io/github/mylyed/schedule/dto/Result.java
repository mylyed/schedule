package io.github.mylyed.schedule.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Created by lilei on 2017/6/23.
 */
@Data
@Builder
public class Result {
    private Boolean success;
    private String msg;

    public static Result success() {
        return Result.builder().success(true).build();
    }
}
