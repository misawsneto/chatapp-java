package com.misaelneto.chatapp.controllers;

import com.misaelneto.chatapp.data.dtos.UserDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class Users {

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getAll(){
        ArrayList<UserDto> users = new ArrayList<UserDto>();
        users.add(new UserDto("1", "misael"));
        return users;
    }
}
