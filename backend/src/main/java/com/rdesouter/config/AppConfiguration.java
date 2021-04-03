package com.rdesouter.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.gmail.Gmail;
import com.rdesouter.EventMessageSyncApplication;
import com.rdesouter.SyncAbstract;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties
public class AppConfiguration extends SyncAbstract {

    //    HikariCP config
    public String databasePassword;
    public String databaseUser;
    public String databasePortNumber;
    public String databaseName;

    //    Security config
    public String frontendUrl;
    public String jwtSecret;
    public long tokenExpirationTime;

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
    public void setDatabasePortNumber(String databasePortNumber) {
        this.databasePortNumber = databasePortNumber;
    }
    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }
    public String getDatabasePassword() {
        return databasePassword;
    }
    public String getDatabasePortNumber() {
        return databasePortNumber;
    }
    public String getDatabaseName() {
        return databaseName;
    }


    @Bean
    public HikariDataSource hikariDataSource(AppConfiguration appConfiguration, BCryptPasswordEncoder bCryptPasswordEncoder){
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
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Gmail gmail() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Gmail
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Bean
    public Calendar calendar() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Calendar
                .Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        String CREDENTIALS_FILE_PATH = "/credentials.json";
        InputStream in = EventMessageSyncApplication.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
//        ClassLoader classLoader = EventMessageSyncApplication.class.getClassLoader();
//        InputStream inputStream = ClassLoader.getSystemResourceAsStream("application.properties");
//        String text = new BufferedReader(
//                new InputStreamReader(inputStream, StandardCharsets.UTF_8))
//                .lines()
//                .collect(Collectors.joining("\n"));

        if (in == null) {
            throw new FileNotFoundException("Credentials file not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets gSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, gSecrets, SCOPES)
                        .setDataStoreFactory(new FileDataStoreFactory(new File("tokens/" + "ronald")))
                        .setAccessType("offline")
                        .build();
        LocalServerReceiver serverReceiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, serverReceiver).authorize("user-test");
    }


}
