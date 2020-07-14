package com.jacoffee.learning.elasticsearch.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * PageList
 * @author jacoffee
 * @date 2020-07-05
 */
@Getter
@Setter
public class PageList<T> {

    private Page page;

    private List<T> data;

    public PageList(Page page, List<T> data) {
        this.page = page;
        this.data = data;
    }

}
