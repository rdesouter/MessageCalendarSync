package com.rdesouter;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.rdesouter.config.AppConfiguration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class Main {


    private final String APPLICATION_NAME = "Noron Gmail API";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final String TOKENS_DIRECTORY_PATH = "tokens/gmail";
    private final String USER_ID = "user-test";

    private final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_READONLY, GmailScopes.GMAIL_SEND, CalendarScopes.CALENDAR, CalendarScopes.CALENDAR_EVENTS_READONLY);
    private final String CREDENTIALS_FILE_PATH = "/credentials.json";

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public HikariDataSource hikariDataSource(AppConfiguration appConfiguration){
        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");

        HikariConfig config = new HikariConfig(props);
        config.addDataSourceProperty("user", appConfiguration.getDatabaseUser());
        config.addDataSourceProperty("password", appConfiguration.getDatabasePassword());
        config.addDataSourceProperty("databaseName", appConfiguration.getDatabaseName());
        config.addDataSourceProperty("portNumber", appConfiguration.getDatabasePortNumber());
        return new HikariDataSource(config);
    }

    @Bean
    public Gmail gmail() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return   new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("Noron Web API")
                .build();
    }

    @Bean
    public Calendar calendar() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName("Noron Web API")
                .build();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GmailExploreApplication.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Credentials file not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets gSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, gSecrets, SCOPES)
                        .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                        .setAccessType("offline")
                        .build();
        LocalServerReceiver serverReceiver = new LocalServerReceiver.Builder().setPort(8888).build();

        return new AuthorizationCodeInstalledApp(flow, serverReceiver).authorize(USER_ID);
    }
}
