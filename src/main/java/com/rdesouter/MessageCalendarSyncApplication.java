package com.rdesouter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessageCalendarSyncApplication extends SyncAbstract {

    public static void main(String[] args) {
        SpringApplication.run(MessageCalendarSyncApplication.class, args);
        System.out.println("API running...");
    }

}
