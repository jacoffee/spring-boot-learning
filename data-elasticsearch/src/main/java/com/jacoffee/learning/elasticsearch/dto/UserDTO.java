package com.jacoffee.learning.elasticsearch.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

/**
 * UserDTO
 * @author jacoffee
 * @date 2020-07-05
 */
@Getter
@Setter
public class UserDTO {

    private String projectId;

    private String uid;

    private String name;

    // yyyy-MM-dd
    private String lastLoginTime;

    @NotNull(message = "分页参数不能为空")
    private Page page;

}
