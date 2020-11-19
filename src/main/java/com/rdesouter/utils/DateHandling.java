package com.rdesouter.utils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

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
}
