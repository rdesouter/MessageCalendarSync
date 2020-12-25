package com.rdesouter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.rdesouter.SyncAbstract;
//import com.rdesouter.dao.repository.MessageRepository;
import com.rdesouter.model.CalendarEvent;
import com.rdesouter.model.MessageConstant;
import com.rdesouter.model.MessageMap;
import com.rdesouter.utils.AppPropertiesValues;
import com.rdesouter.utils.StringHandling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static javax.mail.Message.RecipientType.TO;

@Service
@Transactional
public class MessageService extends SyncAbstract implements MessageConstant {

    @Autowired
    private CalendarService calendarService;
    @Autowired
    private Gmail gmail;
    @Autowired
    private AppPropertiesValues appPropertiesValues;

//    @Autowired
//    private MessageRepository messageRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    public Message sendMail(String messageBody) throws MessagingException, IOException {
        MimeMessage messageContent = createMessageWithMultiPart(SENDER, RECEIVER, SUBJECT, messageBody);
        Message message = createMessage(messageContent);
        message = gmail.users().messages().send("me", message).execute();

        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
        return message;
    }

    private MimeMessage createMessageWithMultiPart(String to, String from, String sub, String bodyText) throws MessagingException {
        Session session = Session.getDefaultInstance(new Properties(), null);

        MimeMessage message = new MimeMessage(session);
        Multipart multipart = new MimeMultipart("alternative");

        MimeBodyPart text = new MimeBodyPart();
        text.setText(BODY_TITLE + "\n" + bodyText, "utf-8");
        multipart.addBodyPart(text);

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent("<h1>" + BODY_TITLE + "</h1><p>" + bodyText + "</p>", "text/html; charset=utf-8");
        multipart.addBodyPart(htmlPart);

        message.setFrom(new InternetAddress(from));
        message.addRecipient(TO, new InternetAddress(to));
        message.setSubject(sub);
        message.setContent(multipart);

        return message;
    }

    private Message createMessage(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }


    /**
     * setQ("labe:yourLabelInGmail")
     * label:yourmail@domain.be
     *
     * .setQ("label:dev@papymousse.be")
     *
     * params can be the same in search
     * for searching in main inbox between two date
     * category:primary after:2020/10/18 before:2020/11/2
     * */
    public List<com.rdesouter.model.Message> getMessages() throws IOException, GeneralSecurityException {

        ListMessagesResponse messageList = gmail.users().messages().list("me").setQ("label:dev@papymousse.be").setMaxResults(5L).execute();
        List<Message> messages = messageList.getMessages();
//        System.out.println("messageList size: " + messageList.size());

        List<com.rdesouter.model.Message> messagesApi = new ArrayList<>();

        if (messageList.isEmpty()) {
            System.out.println("No message found");
        } else {
            System.out.println("Message found:");
            MessageMap messageMap = getMessageMap();

            for (Message message: messages) {
                message = gmail.users().messages().get("me", message.getId()).setFormat("FULL").execute();
                MessagePart messagePart = message.getPayload();


                String subject = "";
                if (messagePart != null) {
                    subject = getSubjectMessage(messagePart, subject);
                    //TODO before add to list check the mime type html/text or plain/text
                    messagesApi.add(getMapForCreateEvent(messageMap, message));

                }
            }
        }

        return messagesApi;
    }

    private void getHtmlTextFromMessageParts(List<MessagePart> messageParts, StringBuilder sb) {
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

    private void getPlainTextFromMessageParts(List<MessagePart> messageParts, StringBuilder sb) {
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

    private HashMap<String,String> extractValueFromMessageBody(String[] contentSplitted, MessageMap messageMap, HashMap<String,String> mapForCreateEvent) {
        for (Map.Entry<String, String> e: messageMap.messageMap.entrySet()){
            Arrays.stream(contentSplitted)
                    .filter(x -> x.contains(e.getValue()))
                    .findFirst()
                    .ifPresent(x -> mapForCreateEvent.put(e.getKey(), StringHandling.extract(x, e.getValue(),false)));
        }
        return mapForCreateEvent;
    }

    private MessageMap getMessageMap() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        MessageMap messageMapped = mapper.readValue(new File(cl.getResource("messageConf.yml").getFile()), MessageMap.class);
        return messageMapped;
    }

    private com.rdesouter.model.Message getMapForCreateEvent(MessageMap messageMap, Message message) {

        StringBuilder sb = new StringBuilder();
        HashMap<String, String> mapForCreateEvent = new HashMap<>();

        if (message.getPayload().getParts() == null){
            String messageBody = new String(Base64.decodeBase64(message.getPayload().getBody().getData()), StandardCharsets.UTF_8);
            System.out.println("utf8 decoded body without parts: \n " + messageBody);
            //TODO need to extract from not part message as well

            //Harcoded just for first test
            CalendarEvent calendarEvent = new CalendarEvent("firstEvent", "rendez-vous pris via l'API");
            com.rdesouter.model.Message message1 = new com.rdesouter.model.Message(message.getPayload().getBody().getData(), calendarEvent);
            return message1;

        }else {
            getPlainTextFromMessageParts(message.getPayload().getParts(), sb);
            System.out.println("base64 bodyparts: " + sb);
            byte[] bodyBytes = Base64.decodeBase64(sb.toString());
            String messageBody = new String(bodyBytes, StandardCharsets.UTF_8);
            System.out.println("utf8 decoded bodyparts: \n"+ messageBody);

            String[] contentSplitted = StringHandling.splitNewLine(messageBody);
            HashMap<String,String> map = extractValueFromMessageBody(contentSplitted, messageMap,mapForCreateEvent);

            if(map.isEmpty()){
                LOGGER.warn("message contains no element for create event \n" + sb);
            }else {
                LOGGER.info("value extracted from message body:" + map);
            }

//            calendarService.createEvent(BEGIN_AT, FINISH_AT);
            return new com.rdesouter.model.Message(sb.toString(), new CalendarEvent("tempId", map.get("address")));

        }
    }

    private String getSubjectMessage(MessagePart messagePart, String subject) {

        List<MessagePartHeader> headers = messagePart.getHeaders();
        for (MessagePartHeader header: headers) {
            //find the subject header
            if (header.getName().equals("Subject")) {
                return header.getValue().trim();
            }
        }
        return subject;
    }

    public void lastMessageTimeStamp() throws IOException, ParseException {
        String logPath = System.getProperty("user.dir") + appPropertiesValues.getConfigValue("log.path");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        Stream<String> stream = Files.lines(Paths.get(logPath));
        // get last element of stream is not natural
        // can be done with skip also
        // long count = stream.count()
        // stream.skip(count - 1).findFirst().get();
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
        } else {
            LOGGER.info("None messages was logged for now");
        }
    }


}
