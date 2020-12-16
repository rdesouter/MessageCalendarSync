package com.rdesouter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventMessageSyncApplication extends SyncAbstract {

    public static void main(String[] args) {
        SpringApplication.run(EventMessageSyncApplication.class, args);
        System.out.println("API running...");
    }

}
