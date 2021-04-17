package com.rdesouter.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class DateHandling {

    public static String getDateWithTimeZone(int year, int month, int day, int hour, int minute, String zoneOffset) {
        /*
         * Find Time Zone ID:
         * -----------------
         * https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
         * */
        LocalDateTime date = LocalDateTime.of(year, month, day, hour, minute,1);  // Create a date object
        ZoneId zone = ZoneId.of(zoneOffset);
        ZoneOffset zoneOffSet = zone.getRules().getOffset(date);
        OffsetDateTime dateTimeWithOffset = OffsetDateTime.of(date, zoneOffSet);
        System.out.println("date with offset: " + dateTimeWithOffset);
        return dateTimeWithOffset.toString();
    }

    public static LocalDateTime transformDateStringForEvent(String pattern, String value, HashMap<String,String> map){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String begin = map.get(value).trim();
        LocalDate localDate = LocalDate.parse(begin, formatter);

//        LocalTime localTime = LocalTime.parse(map.get("beginHour").trim(), DateTimeFormatter.ofPattern("HH:mm"));
        return LocalDateTime.of(
                localDate.getYear(),
                localDate.getMonthValue(),
                localDate.getDayOfMonth(),
                7,
                0,
                0
        );
    }
}
