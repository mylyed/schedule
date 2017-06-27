package io.github.mylyed.schedule.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by lilei on 2017/6/26.
 */
@Data
public class PageArgs {
    @NotNull
    @Min(0)
    private Integer draw;
    @NotNull
    @Min(0)
    private Integer start;
    @NotNull
    @Min(0)
    private Integer length;
}
