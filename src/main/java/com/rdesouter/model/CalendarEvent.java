package com.rdesouter.model;

import java.util.ArrayList;
import java.util.Map;

public class CalendarEvent {

    private String calendarId;
    private String summary;
    private String location;
    private String description;
    private DateWithTimeZone start;
    private DateWithTimeZone end;
    private ArrayList<String> attendes;
    private Map<String,Integer> reminders;


    public CalendarEvent(String calendarId, String summary, String location, String description, DateWithTimeZone start, DateWithTimeZone end) {
        this.calendarId = calendarId;
        this.summary = summary;
        this.location = location;
        this.description = description;
        this.start = start;
        this.end = end;
    }

    public CalendarEvent(String calendarId, String summary, String location, String description, DateWithTimeZone start, DateWithTimeZone end, ArrayList<String> attendes) {
        this.calendarId = calendarId;
        this.summary = summary;
        this.location = location;
        this.description = description;
        this.start = start;
        this.end = end;
        this.attendes = attendes;
    }

    public CalendarEvent(String calendarId, String summary, String location, String description, DateWithTimeZone start, DateWithTimeZone end, Map<String, Integer> reminders) {
        this.calendarId = calendarId;
        this.summary = summary;
        this.location = location;
        this.description = description;
        this.start = start;
        this.end = end;
        this.reminders = reminders;
    }

    public CalendarEvent(String calendarId, String summary, String location, String description, DateWithTimeZone start, DateWithTimeZone end, ArrayList<String> attendes, Map<String, Integer> reminders) {
        this.calendarId = calendarId;
        this.summary = summary;
        this.location = location;
        this.description = description;
        this.start = start;
        this.end = end;
        this.attendes = attendes;
        this.reminders = reminders;
    }

    public String getCalendarId() {
        return calendarId;
    }
    public String getSummary() {
        return summary;
    }
    public String getLocation() {
        return location;
    }
    public String getDescription() {
        return description;
    }
    public DateWithTimeZone getStart() {
        return start;
    }
    public DateWithTimeZone getEnd() {
        return end;
    }
    public ArrayList<String> getAttendes() {
        return attendes;
    }
    public Map<String, Integer> getReminders() {
        return reminders;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setStart(DateWithTimeZone start) {
        this.start = start;
    }
    public void setEnd(DateWithTimeZone end) {
        this.end = end;
    }
    public void setAttendes(ArrayList<String> attendes) {
        this.attendes = attendes;
    }
    public void setReminders(Map<String, Integer> reminders) {
        this.reminders = reminders;
    }


}
