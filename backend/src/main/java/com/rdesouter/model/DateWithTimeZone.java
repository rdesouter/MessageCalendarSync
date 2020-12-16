package com.rdesouter.model;

public class DateWithTimeZone {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private String timeZoneId;

    public DateWithTimeZone(int year, int month, int day, int hour, int minute, String timeZoneId) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.timeZoneId = timeZoneId;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }
    public int getYear() {
        return year;
    }
    public int getMonth() {
        return month;
    }
    public int getDay() {
        return day;
    }
    public int getHour() {
        return hour;
    }
    public int getMinute() {
        return minute;
    }


    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return "DateWithTimeZone{" +
                "timeZoneId='" + timeZoneId + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", minute=" + minute +
                '}';
    }
}
