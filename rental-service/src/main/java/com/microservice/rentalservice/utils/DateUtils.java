package com.microservice.rentalservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

@Component
@Slf4j
public class DateUtils {
    public static String now(){
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }

    public static boolean matchingDate(String endDate) {
        Calendar calendar = new GregorianCalendar();

        calendar.add(Calendar.DATE, -1);

        return Objects.equals(endDate, new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()));
    }
}
