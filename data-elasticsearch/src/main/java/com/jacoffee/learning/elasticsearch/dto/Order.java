package com.jacoffee.learning.elasticsearch.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Order
 * @author jacoffee
 * @date 2020-07-05
 */
@Getter
@Setter
public class Order {

    public enum OrderDirection {
        ASC, DESC;
    }

    private String name;

    private OrderDirection direction;

}
