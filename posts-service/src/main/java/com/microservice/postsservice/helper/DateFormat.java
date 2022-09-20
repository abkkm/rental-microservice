package com.microservice.postsservice.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class DateFormat {
    public static String now(){
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }
}
