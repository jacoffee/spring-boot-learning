package com.jacoffee.learning.elasticsearch.service;

import com.jacoffee.learning.elasticsearch.dto.PageList;
import com.jacoffee.learning.elasticsearch.dto.UserDTO;
import com.jacoffee.learning.elasticsearch.model.UserDocument;

/**
 * UserService
 * @author jacoffee
 * @date 2020-07-05
 */
public interface UserService {

    PageList<UserDocument> searchPageList(UserDTO userDTO) throws Exception;

}
