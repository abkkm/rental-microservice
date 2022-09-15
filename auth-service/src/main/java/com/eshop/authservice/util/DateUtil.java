package com.eshop.authservice.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String dateNow() {
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }
}