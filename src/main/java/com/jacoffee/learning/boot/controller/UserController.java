package com.jacoffee.learning.boot.controller;

import com.jacoffee.learning.boot.dto.UserDTO;
import com.jacoffee.learning.boot.dto.WebApiResponse;
import com.jacoffee.learning.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

/**
 * UserController
 * @author jacoffee
 * @date 2020-07-05
 */
@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebApiResponse search(
        @RequestBody @Valid UserDTO userDTO
    ) throws Exception {
        return WebApiResponse.success(userService.searchPageList(userDTO));
    }

}
