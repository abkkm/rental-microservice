package com.microservice.messageservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MessageController {

    @GetMapping("/status")
    public String checkStatusService(){
        return  "This is message service";
    }

}
