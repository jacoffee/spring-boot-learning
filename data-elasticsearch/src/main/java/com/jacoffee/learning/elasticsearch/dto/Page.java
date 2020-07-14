package com.jacoffee.learning.elasticsearch.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * Page
 * @author jacoffee
 * @date 2020-07-05
 */
@Getter
@Setter
public class Page {

    // 起始页，默认为1
    private Integer pageNum = 1;

    // 每页数据大小，默认为10
    private Integer pageSize = 10;

    // 当次查询总行数
    private Long totalRows;

    // 当次查询总页数
    private Long totalPage;

    // 排序
    public List<Order> orders;

    // search after值
    private Object[] searchAfter = new Object[0];

}
