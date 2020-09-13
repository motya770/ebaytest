package com.ebay.demo.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Utils {
    public static LocalDateTime localDateTimeFromMill(Long mill){
        LocalDateTime date =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(mill), ZoneId.systemDefault());
        return date;
    }

    public static long getMill(LocalDateTime localDateTime){
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
