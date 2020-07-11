package com.jacoffee.learning.boot.model;

import static com.jacoffee.learning.boot.utils.Constants.ES_ID;

import com.jacoffee.learning.boot.annotations.SortValues;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * ESDocument
 * @author jacoffee
 * @date 2020-07-05
 */
@Setter
@Getter
public abstract class ESDocument {

    // 用于存储sort value
    @SortValues
    protected Object[] sort;

    // 用作search_after的tie_breaker_id
    @Field(type = FieldType.Keyword, name = ES_ID)
    protected String esId;

}
