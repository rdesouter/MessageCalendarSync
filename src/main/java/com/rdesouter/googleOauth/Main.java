package com.rdesouter.googleOauth;

import com.google.gson.Gson;
import com.rdesouter.calendar.CalendarExploreApplication;
import okhttp3.*;
import org.springframework.boot.SpringApplication;

import java.io.IOException;
import java.util.Objects;

public class Main {

    private static String refreshToken;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void main(String[] args) throws IOException {
        SpringApplication.run(CalendarExploreApplication.class, args);
        System.out.println("API Runing....");

        refreshToken();
//        firstRequest();
//        insertEvent();

    }

    private static void insertEvent() throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder url = Objects.requireNonNull(HttpUrl
                .parse("https://www.googleapis.com/calendar/v3/calendars/primary/events"))
                .newBuilder();

        RequestBody body = RequestBody.create(data(), JSON);

//        System.out.println("body: " + body);
        Request insertEvent = new Request.Builder()
                .url(url.build().toString())
                .post(body)
                .addHeader("Authorization", "Bearer " + refreshToken)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(insertEvent).execute();
        System.out.println(response.body().string());

    }

    private static String data() {
        return "{'summary': 'Insert via serveur'," +
                "'location': 'Rue Camille Vandervost 4, 1350 Orp-Jauche, Belgium'," +
                "'description': 'Un petit test insertion'," +
                "'end':" + "{"+
                    "'dateTime': '2020-10-24T17:00:00+02:00'," +
                    "'timeZone': 'Europe/Brussels'" +
                "},"+
                "'start':" +"{"+
                    "'dateTime': '2020-10-23T09:00:00+02:00'," +
                    "'timeZone': 'Europe/Brussels'}" +
                "}";
    }


    private static void firstRequest() throws IOException {


        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder url = Objects.requireNonNull(HttpUrl
                .parse("https://www.googleapis.com/calendar/v3/calendars/primary/events"))
                .newBuilder();
//        url.addQueryParameter("q", "SEARCH_TERM");

        Request request = new Request.Builder()
                .url(url.build().toString())
                .get()
                .addHeader("Authorization", "Bearer " + refreshToken)
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    private static void refreshToken() throws IOException {

        OkHttpClient client = new OkHttpClient();

        String CLIENT_ID = "YOUR_CLIENT_ID";
        String CLIENT_SECRET = "YOUR_CLIENT_SECRET";

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("client_id", CLIENT_ID)
                .addFormDataPart("client_secret", CLIENT_SECRET)
                .addFormDataPart("refresh_token", "REFRESH_TOKEN_NEED_TO_BE_SET")
                .addFormDataPart("grant_type", "refresh_token")
                .build();

        Request request = new Request.Builder()
                .url("https://www.googleapis.com/oauth2/v4/token")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

//        System.out.println(response.body().string());
        String json = response.body().string();
        ResponseBody responseBody = new Gson().fromJson(json, ResponseBody.class);
        System.out.println(responseBody);
        refreshToken = responseBody.access_token;
    }
}
