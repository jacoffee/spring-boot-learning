package com.jacoffee.learning.elasticsearch.repository;

import com.jacoffee.learning.elasticsearch.dto.Page;
import com.jacoffee.learning.elasticsearch.dto.PageList;
import com.jacoffee.learning.elasticsearch.model.ESDocument;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.IOException;

/**
 * 对于默认Repository进行扩展
 * @see <a href="https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/">spring repository</a>
 * @author jacoffee
 * @date 2020-07-05
 */
public interface CustomizedRepository<T extends ESDocument> {

    PageList<T> doSearchAfterPageQuery(
        QueryBuilder queryBuilder, Page page, Class<T> clazz
    ) throws IOException;

    PageList<T> doNormalPageQuery(
        QueryBuilder queryBuilder, Page page, Class<T> clazz
    );

}
