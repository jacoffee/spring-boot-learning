package com.jacoffee.learning.boot.service;

import com.jacoffee.learning.boot.dto.PageList;
import com.jacoffee.learning.boot.dto.UserDTO;
import com.jacoffee.learning.boot.model.UserDocument;

/**
 * UserService
 * @author jacoffee
 * @date 2020-07-05
 */
public interface UserService {

    PageList<UserDocument> searchPageList(UserDTO userDTO) throws Exception;

}
