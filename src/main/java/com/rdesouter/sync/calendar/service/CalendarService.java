package com.rdesouter.sync.calendar.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.rdesouter.sync.SyncAbstract;
import com.rdesouter.sync.CredentialsProvider;
import com.rdesouter.utils.DateHandling;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

@Service
public class CalendarService extends SyncAbstract {

    private static Calendar getCalendarService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar calendarService = new Calendar
                .Builder(HTTP_TRANSPORT,JSON_FACTORY, CredentialsProvider.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
       return calendarService;
    }

    /**
     * get Timzone list https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
     * */
    public void createEvent(String beginAt, String finishAt) throws IOException, GeneralSecurityException {
        Event insertEvent = new Event()
                .setSummary("L'heure de faire DODO")
                .setLocation("Rue du chêne 6, 1000 Bruxelles, Belgique")
                .setDescription("Just to see if its works ! -)");

        DateTime startDateTime = new DateTime(beginAt);
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime);
//                .setTimeZone("Europe/Brussels");
        insertEvent.setStart(start);

        DateTime endDateTime = new DateTime(finishAt);
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime);
//                .setTimeZone("Europe/Brussels");
        insertEvent.setEnd(end);

        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail("titi@gmail.com"),
                new EventAttendee().setEmail("toto@gmail.com"),
        };
        insertEvent.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(30),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        insertEvent.setReminders(reminders);

//        String calendarId = "primary";

        insertEvent = getCalendarService().events().insert("primary", insertEvent).execute();
        System.out.printf("Event created: %s\n", insertEvent.getHtmlLink());
    }

}
