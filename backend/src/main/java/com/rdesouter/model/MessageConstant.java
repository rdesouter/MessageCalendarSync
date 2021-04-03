package com.rdesouter.model;

import java.time.LocalDateTime;

public interface MessageConstant {

    String SENDER = "ron.desouter@gmail.com";
    String RECEIVER = "dev@papymousse.be";
    String SUBJECT = "Message sent with gmail API";
    String BODY_TITLE = "Congratutaltions";
    String BODY_TEXT = "" +
            "Résumé : rendez-vous pris via l'API\n" +
            "Adresse : Avenue Rogier 1, 1030 Schaerbeek, Belgique\n" +
            "\n" +
            "Téléphone : 0484.726.479\n" +
            "Date de début : 01.04.2021\n" +
            "Heure de début : 7h30\n" +
            "Date de fin : 02.04.2020\n" +
            "sending at: " + LocalDateTime.now();

    String HEADERS_FROM = "From";
    String HEADERS_TO = "To";
    String HEADERS_SENDING_DATE = "Date";
    String HEADERS_SUBJECT = "Subject";

}
