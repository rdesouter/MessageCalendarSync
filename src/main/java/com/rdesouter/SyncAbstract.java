package com.rdesouter;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.rdesouter.utils.DateHandling;
import com.rdesouter.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class SyncAbstract {

    protected static final String APPLICATION_NAME = "Message_Calendar_Sync_API";

    protected static String BEGIN_AT = DateHandling.getDateWithTimeZone(2020,10,20,0,0,"Australia/North");
    protected static String FINISH_AT = DateHandling.getDateWithTimeZone(2020,10,20,1,0, "Australia/North");

    protected static HashMap<String, String> messageMapForEvent = new HashMap<>();
}
