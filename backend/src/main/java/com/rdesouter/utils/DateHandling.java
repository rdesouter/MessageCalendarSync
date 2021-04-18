package com.rdesouter.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

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

    public static LocalDateTime transformDateStringForEvent(String patternDate, String patternTime, String valueDate, String valueTime){
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern(patternDate);
        //TODO verify patternDate otherwise create event at 01.12.[year_of_request]
        LocalDate localDate = LocalDate.parse(valueDate.trim(), formatDate);

        DateTimeFormatter formatTime = DateTimeFormatter.ofPattern(patternTime);
        try {
            LocalTime localTime = LocalTime.parse(valueTime.trim(), formatTime);
            return LocalDateTime.of(
                    localDate.getYear(),
                    localDate.getMonthValue(),
                    localDate.getDayOfMonth(),
                    localTime.getHour(),
                    localTime.getMinute(),
                    localTime.getSecond()
            );
        }catch(RuntimeException e){
            LocalDateTime now = LocalDateTime.now();
            return LocalDateTime.of(
                    now.getYear(),
                    12,
                    1,
                    0,
                    0,
                    0
            );
        }
    }
}
