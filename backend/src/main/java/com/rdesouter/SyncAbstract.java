package com.rdesouter;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.gmail.GmailScopes;
import com.rdesouter.utils.DateHandling;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SyncAbstract {

    protected static final String APPLICATION_NAME = "Message_Calendar_Sync_API";

    protected static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    protected static final List<String> SCOPES = Arrays.asList(
            GmailScopes.GMAIL_READONLY,
            GmailScopes.GMAIL_SEND,
            CalendarScopes.CALENDAR,
            CalendarScopes.CALENDAR_EVENTS_READONLY);

    protected static String BEGIN_AT = DateHandling.getDateWithTimeZone(2020,10,20,0,0,"Australia/North");
    protected static String FINISH_AT = DateHandling.getDateWithTimeZone(2020,10,20,1,0, "Australia/North");

    protected static HashMap<String, String> messageMapForEvent = new HashMap<>();
}
