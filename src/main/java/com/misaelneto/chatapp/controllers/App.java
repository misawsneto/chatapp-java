package com.misaelneto.chatapp.controllers;

import com.misaelneto.chatapp.data.dtos.InitialDataDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class App {

    @RequestMapping(value = "/init", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public InitialDataDto getInitialData(){
        return null;
    }

}
