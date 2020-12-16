package com.rdesouter.calendar;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import org.springframework.boot.SpringApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.*;
import java.util.*;


public class CalendarExploreApplication {

    private static final String APPLICATION_NAME = "Noron Calendar API";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        //Load client secrects
        InputStream in = CalendarExploreApplication.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets googleClientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, googleClientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void test() throws GeneralSecurityException, IOException {

        String beginAt = getDateWithTimeZone(2020,10,20,0,0,"Australia/North");
        System.out.println(beginAt);
        String finishAt = getDateWithTimeZone(2020,10,20,1,0, "Australia/North");

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT)).setApplicationName(APPLICATION_NAME).build();
//
//        createEvent(service);
//        getCalendartList(service);
        getPrimaryCalendarEvents(10, service);
    }

    private static String getDateWithTimeZone(int year, int month, int day, int hour, int minute, String zoneOffset) {
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

    private static void createEvent(Calendar service, String beginAt, String finishAt) throws IOException {
        Event insertEvent = new Event()
                .setSummary("L'heure de faire DODO")
                .setLocation("Rue du chêne 6, 1000 Bruxelles, Belgique")
                .setDescription("Just to see if its works ! -)");

        /*
         * get Timzone list https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
         * */

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

        insertEvent = service.events().insert("primary", insertEvent).execute();
        System.out.printf("Event created: %s\n", insertEvent.getHtmlLink());
    }

    private static void getAclRule(Calendar service) throws IOException {
        // Retrieve access rule
        Acl rule = service.acl().list("primary").execute();
        AclRule aclRule = service.acl().get("primary", "user:ron.desouter@gmail.com").execute();
        System.out.println(aclRule.getId() + ": " + aclRule.getRole());
    }
}

