package com.rdesouter.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;

import static javax.mail.Message.RecipientType.TO;

@SpringBootApplication
public class GmailExploreApplication implements MessageConstant{

    private static final String APPLICATION_NAME = "Noron Gmail API";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens/gmail";
    private static final String USER_ID = "user-test";

    private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_READONLY, GmailScopes.GMAIL_SEND, CalendarScopes.CALENDAR, CalendarScopes.CALENDAR_EVENTS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
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

    public static void main(String[] args) throws GeneralSecurityException, IOException, MessagingException {
        SpringApplication.run(GmailExploreApplication.class, args);
        System.out.println("Google's API Running...");

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail gmailService =
                new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                            .setApplicationName(APPLICATION_NAME)
                            .build();
        getMessages(gmailService);
//        sendGmail(gmailService);

    }

    private static void getMessages(Gmail gmailService) throws IOException {
        /*
        * setQ("labe:yourLabelInGmail")
        * params can be the same in search
        * for searching in main inbox between two date
        * category:primary after:2020/10/18 before:2020/11/2
        * label:yourmail@domain.be
         * */
        ListMessagesResponse messageList = gmailService.users().messages().list("me").setQ("label:dev@papymousse.be after:2020/11/04").setMaxResults(2L).execute();
        List<Message> messages = messageList.getMessages();

        if (messageList.isEmpty()) {
            System.out.println("No message found");
        } else {
            System.out.println("Message found:");

            for (Message message: messages) {
                message = gmailService.users().messages().get("me", message.getId()).setFormat("FULL").execute();
                MessagePart messagePart = message.getPayload();
                String messageContent = "";
                String subject = "";
                    if (messagePart != null) {
                        //find the subject header
                        List<MessagePartHeader> headers = messagePart.getHeaders();
                        for (MessagePartHeader header: headers) {
                            //find the subject header
                            if (header.getName().equals("Subject")) {
                                subject = header.getValue().trim();
                                System.out.println(header.getValue().trim());
                                break;
                            }
                        }

                        StringBuilder sb = new StringBuilder();

                        if (message.getPayload().getParts() == null){
                            String body = new String(Base64.decodeBase64(message.getPayload().getBody().getData()), StandardCharsets.UTF_8);
                            System.out.println("message without parts: \n " + body);
                        }else {
                            getPlainTextFromMessageParts(message.getPayload().getParts(), sb);
//                            getHtmlTextFromMessageParts(message.getPayload().getParts(), sb);
                            System.out.println("base64 bodyparts: " + sb);
                            byte[] bodyBytes = Base64.decodeBase64(sb.toString());
                            String text = new String(bodyBytes, StandardCharsets.UTF_8);
                            System.out.println("utf8 decoded bodyparts: \n"+ text);

                            String[] contentSplitted = StringHandling.splitNewLine(text);

                            List<String> elements = new ArrayList<>();

                            ClassLoader cL = Thread.currentThread().getContextClassLoader();

                            File file = new File(cL.getResource("eventTest.yml").getFile());
                            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                            EventTest event = objectMapper.readValue(file, EventTest.class);
                            System.out.println(event);

                            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                            mapper.findAndRegisterModules();
                            EventMap eventMap = mapper.readValue(new File(cL.getResource("eventTest1.yml").getFile()), EventMap.class);

                            //extract all value from key in eventTest1.yml and add to map
                            // TODO preferably convert into hashmap
                            for (Map.Entry<String, String> e: eventMap.messageMap.entrySet()){
                                Arrays.stream(contentSplitted).filter(x -> x.contains(e.getValue())).findFirst().filter(x -> elements.add(StringHandling.extract(x, e.getValue(),false)));
                            }


                            Arrays.stream(contentSplitted).filter(x -> x.contains("Téléphone")).findFirst().filter(x -> elements.add(StringHandling.extract(x,"Téléphone :",false)));
                            String phoneExtract = StringHandling.extract(text, "Téléphone :", false);
                            System.out.println(phoneExtract);

                        }
                    }
            }
        }
    }

    private static void getPlainTextFromMessageParts(List<MessagePart> messageParts, StringBuilder sb) {
        if (messageParts != null) {
            for (MessagePart messagePart: messageParts) {
//                System.out.println(messagePart);
                if (messagePart.getMimeType().equals("text/plain")) {
//                    System.out.println(messagePart.getBody().getData());
                    sb.append(messagePart.getBody().getData());
                }
                // use recursive meth to get all parts content text/plain
                if (messagePart.getParts() != null) {
                    getPlainTextFromMessageParts(messagePart.getParts(), sb);
                }
            }
        }
    }

    private static void getHtmlTextFromMessageParts(List<MessagePart> messageParts, StringBuilder sb) {
        if (messageParts != null) {
            for (MessagePart messagePart: messageParts) {
                System.out.println(messagePart);
                if(messagePart.getMimeType().equals("text/html")){
                    System.out.println(messagePart.getBody().getData());
                    sb.append(messagePart.getBody().getData());
                }
                if (messagePart.getParts() != null) {
                    getPlainTextFromMessageParts(messagePart.getParts(), sb);
                }
            }
        }
    }


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
