package com.rdesouter.sync.calendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class CalendarExploreApplication {

    public static void main(String[] args) throws GeneralSecurityException, IOException {
//        SpringApplication.run(CalendarExploreApplication.class, args);
//        createEvent(service);
//        getCalendartList(service);
//        getPrimaryCalendarEvents(10, service);
    }

    private static void getCalendartList(Calendar service) throws IOException {
        // Iterate through entries in calendar list
        String pageToken = null;
        do {
            CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
            List<CalendarListEntry> items = calendarList.getItems();

            for (CalendarListEntry calendarListEntry : items) {
                System.out.println(calendarListEntry.getSummary());
            }
            pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);

        CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
        System.out.println(calendarList);
    }

    private static void getPrimaryCalendarEvents(int eventsNumber,Calendar service) throws IOException {
        /*
        *  List the next events {eventsNumber} from the primary calendar.
        *  To retrieve some other calendar Id go to settings in browser integrate calendar / primary for own email address
        *
        *   Example of Id to get holday in belgium
        *   fr.be#holiday@group.v.calendar.google.com
        * */

        DateTime now = new DateTime(System.currentTimeMillis());

        Events events = service.events().list("primary")
                .setMaxResults(eventsNumber)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start1 = event.getStart().getDateTime();
                if (start1 == null) {
                    start1 = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start1);
            }
        }
    }

    private static void createFastEvent(Calendar service) throws IOException {
        // Create fast event
        String eventText = "Appointment at Somewhere on June 3rd 10am-10:25am";
        Event createdEvent =
                service.events().quickAdd("primary",eventText).execute();
        System.out.println(createdEvent.getId());
    }

    private static void getAclRule(Calendar service) throws IOException {
        // Retrieve access rule
        Acl rule = service.acl().list("primary").execute();
        AclRule aclRule = service.acl().get("primary", "user:ron.desouter@gmail.com").execute();
        System.out.println(aclRule.getId() + ": " + aclRule.getRole());
    }
}

