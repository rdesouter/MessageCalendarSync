package com.rdesouter.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.rdesouter.utils.StringHandling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;

import static javax.mail.Message.RecipientType.TO;

@SpringBootApplication
public class GmailExploreApplication implements MessageConstant{

    private static final String APPLICATION_NAME = "Noron Gmail API";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();


    public static void main(String[] args) throws GeneralSecurityException, IOException, MessagingException {
        SpringApplication.run(GmailExploreApplication.class, args);
        System.out.println("Google's API Running...");

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        CredentialsProvider.getCredentials(HTTP_TRANSPORT);

//        Gmail gmailService =
//                new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, CredentialsProvider.getCredentials(HTTP_TRANSPORT))
//                            .setApplicationName(APPLICATION_NAME)
//                            .build();
//        System.out.println(gmailService);
//        getMessages(gmailService);
//        sendGmail(gmailService);

    }

    // METH FOR SENDING
    private static void sendGmail(Gmail gmailService) throws MessagingException, IOException {
//        MimeMessage mimeMessage = createEmail("ron.desouter@gmail.com", "dev@papymousse.be", "First send with api", "Send from dev email");
        MimeMessage m = createMessageWithMultiPart(SENDER, RECEIVER, SUBJECT);
        sendMessage(gmailService, "me", m);
    }

    public static MimeMessage createMessageWithMultiPart(String to, String from, String sub) throws MessagingException {
        Session session = Session.getDefaultInstance(new Properties(), null);

        MimeMessage message = new MimeMessage(session);
        Multipart multipart = new MimeMultipart("alternative");

        MimeBodyPart text = new MimeBodyPart();
        text.setText(BODY_TITLE + "\n" + BODY_TEXT, "utf-8");
        multipart.addBodyPart(text);

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent("<h1>" + BODY_TITLE + "</h1><p>" + BODY_TEXT + "</p>", "text/html; charset=utf-8");
        multipart.addBodyPart(htmlPart);

        message.setFrom(new InternetAddress(from));
        message.addRecipient(TO, new InternetAddress(to));
        message.setSubject(sub);
        message.setContent(multipart);

        return message;
    }

    public static MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {
        Session session = Session.getDefaultInstance(new Properties(), null);
        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);

        return email;
    }

    private static Message sendMessage(Gmail service, String userId, MimeMessage emailContent) throws MessagingException, IOException {
        Message message = createMessageWithEmail(emailContent);
        message = service.users().messages().send(userId, message).execute();

        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
        return message;
    }

    private static Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

}
