package com.rdesouter.utils;

import com.rdesouter.service.SyncMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

public class LogUtil {

    @Autowired
    private static AppPropertiesValues appPropertiesValues;

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncMessageService.class);

    public static Long lastMessageTimeStamp() throws IOException, ParseException {
        String logPath = System.getProperty("user.dir") + appPropertiesValues.getConfigValue("log.path");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        Stream<String> stream = Files.lines(Paths.get(logPath));
        String lastTimeStamp = stream
                .reduce((first,second)-> second)
                .map(line ->StringHandling.extract(line, "INFO", true))
                .orElse(null);

        //TODO only if null but need to check if after INFO
        // not equal to "no message was logged"
        if (lastTimeStamp != null){
            Date date = dateFormat.parse(lastTimeStamp);
            Timestamp timestamp = new Timestamp(date.getTime());
            //google filter only allow epoch timestamp in second
            Long timeStampInSecond = timestamp.getTime()/1000;
            return timeStampInSecond;
        } else {
            LOGGER.info("None messages was logged for now");
            return null;
        }
    }
}
