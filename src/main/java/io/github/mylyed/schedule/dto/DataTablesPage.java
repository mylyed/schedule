package io.github.mylyed.schedule.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by lilei on 2017/6/26.
 */
@Data
public class DataTablesPage<T> {

    private List<T> data;
    private Integer draw;
    private String error;
    private Long recordsTotal;
    private Long recordsFiltered;

    public DataTablesPage(Page<T> page, Integer draw) {
        data = page.getContent();
        this.draw = draw;
        error = null;
        recordsFiltered = page.getTotalElements();
        recordsTotal = page.getTotalElements();
    }

}
