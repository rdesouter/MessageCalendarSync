package com.rdesouter.service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.rdesouter.SyncAbstract;
import com.rdesouter.model.*;
import com.rdesouter.utils.DateHandling;
import com.rdesouter.utils.LogUtil;
import com.rdesouter.utils.StringHandling;
import com.rdesouter.utils.SyncMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SyncMessageService extends SyncAbstract implements MessageConstant {

    @Autowired
    private SyncEventService syncEventService;
    @Autowired
    private Gmail gmail;
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncMessageService.class);

    public Message sendMail(String messageBody) throws MessagingException, IOException {
        MimeMessage messageContent = SyncMessageUtil.createMessageWithMultiPart(SENDER, RECEIVER, SUBJECT, messageBody);
        Message message = SyncMessageUtil.createMessage(messageContent);
        message = gmail.users().messages().send("me", message).execute();

        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
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
    public List<SyncMessage> getMessages() throws IOException {
        //TODO set setQ and MaxResult in param of param for user store in DB
        // of set only setQ with label and timestamp
        ListMessagesResponse messagesResponse = gmail
                .users().messages().list("me")
                .setQ("label:dev@papymousse.be")
                .setMaxResults(5L).execute();
        List<SyncMessage> syncMessages = new ArrayList<>();

        if (!messagesResponse.isEmpty()) {
//            System.out.println("Message found:");
            for (Message message: messagesResponse.getMessages()) {
                message = gmail
                        .users()
                        .messages()
                        .get("me", message.getId())
                        .setFormat("FULL")
                        .execute();
                List<MessagePart> syncableMessageParts = new ArrayList<>();
                MessagePart messagePart = message.getPayload();
                String subject = getHeaderValueFromMessage(messagePart, HEADERS_SUBJECT);
                String sendingDate = getHeaderValueFromMessage(messagePart, HEADERS_SENDING_DATE);
                String messageFrom = getHeaderValueFromMessage(messagePart, HEADERS_FROM);
                String messageTo = getHeaderValueFromMessage(messagePart, HEADERS_TO);

                MessageConfig messageConfig = SyncMessageUtil.getMessageConfigMapped();
                HashMap<String, String> extracted;
                Optional<List<MessagePart>> isMultiPart = Optional.ofNullable(messagePart.getParts());

                if (isMultiPart.isPresent()){
                    syncableMessageParts = excludeAttachment(messagePart);
                    extracted = getValueExtracted(syncableMessageParts, messageConfig);
//                    System.out.println(extracted);
                    if (extracted.size() > 0 && extracted.size() < messageConfig.map.size()) {
                        // only store subject, messageFrom, sendingDate + payload but not create event
                        List<MessageBody> messageBodies = new ArrayList<>();
                        syncableMessageParts.forEach(
                                part -> messageBodies.add(new MessageBody(
                                        0,
                                        part.getBody().getData(),
                                        part.getMimeType()))
                        );
                        MessagePortion messagePortion = new MessagePortion(
                                0,
                                subject,
                                messageFrom,
                                sendingDate,
                                messageBodies);

                        SyncMessage partialSyncMessage = new SyncMessage(
                                messagePortion,
                                new SyncEvent()// need a method to extract and check value from from message depends on value config file
                        );
                        syncMessages.add(partialSyncMessage);

                    } else if (extracted.size() == 0) {
                        // only store subject, comingFrom, date but not payload + not create event
                        MessagePortion messagePortion = new MessagePortion(
                                0,
                                subject,
                                messageFrom,
                                sendingDate,
                                Arrays.asList(new MessageBody()));
                        SyncMessage notSyncMessage = new SyncMessage(
                                messagePortion,
                                new SyncEvent()
                        );
                        syncMessages.add(notSyncMessage);
                    } else if (extracted.size() == messageConfig.map.size()) {
                        // store data and create event
                        List<MessageBody> messageBodies = new ArrayList<>();
                        syncableMessageParts.forEach(
                                part -> messageBodies.add(new MessageBody(
                                        0,
                                        part.getBody().getData(),
                                        part.getMimeType()))
                        );
                        MessagePortion messagePortion = new MessagePortion(
                                0,
                                subject,
                                messageFrom,
                                sendingDate,
                                messageBodies);

                        SyncMessage fullySyncMessage = new SyncMessage(
                                messagePortion,
                                new SyncEvent(
                                        UUID.randomUUID().toString(),
                                        subject,
                                        extracted.get("address"),
                                        DateHandling.transformDateStringForEvent("dd.MM.yyyy", "begin", extracted),
                                        DateHandling.transformDateStringForEvent("dd.MM.yyyy", "end", extracted),
                                        Arrays.asList(messageFrom, messageTo),
                                        "extra-info:" + extracted.get("phone"))
                        );
                        syncMessages.add(fullySyncMessage);

                    }

                } else {
                    extracted = getValueExtracted(messagePart, messageConfig);
                }
            }
        }
        return syncMessages;
    }


    private HashMap<String,String> getValueExtracted(MessagePart part, MessageConfig messageConfig) {
        byte[] bodyBytes = part.getBody().decodeData();
        String messageBody = new String(bodyBytes, StandardCharsets.UTF_8);
        String[] contentSplitted = StringHandling.splitNewLine(messageBody);
        return extractValueFromMessageBody(contentSplitted, messageConfig);
    }

    private HashMap<String,String> getValueExtracted(List<MessagePart> parts, MessageConfig messageConfig){
//        StringBuilder sb = new StringBuilder();
        for(MessagePart part: parts) {
            byte[] bodyBytes = part.getBody().decodeData();
            String messageBody = new String(bodyBytes, StandardCharsets.UTF_8);
            String[] contentSplitted = StringHandling.splitNewLine(messageBody);
            return extractValueFromMessageBody(contentSplitted, messageConfig);
        }
//        parts.forEach(part -> {
//            byte[] bodyBytes = part.getBody().decodeData();
//            String messageBody = new String(bodyBytes, StandardCharsets.UTF_8);
//            String[] contentSplitted = StringHandling.splitNewLine(messageBody);
//
//            extractValueFromMessageBody(contentSplitted);
//        });
        return null;

//        Harcoded just for first test
//            SyncEvent syncEvent = new SyncEvent("firstEvent", "rendez-vous pris via l'API");
//            SyncMessage syncMessage1 = new SyncMessage(message.getPayload().getBody().getData(), syncEvent);

//            if(map.isEmpty()){
//                LOGGER.warn("message contains no element for create event \n" + sb);
//                //TODO throw custom exception
//                return new SyncMessage(sb.toString());
//            }else {
//                LOGGER.info("value extracted from message body:" + map);
//                return new SyncMessage(sb.toString(), new SyncEvent("tempId", map.get("address")));
//            }
    }

    private HashMap<String,String> extractValueFromMessageBody(String[] contentSplitted, MessageConfig messageConfig) {
        HashMap<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> configElement: messageConfig.map.entrySet()){
            Arrays.stream(contentSplitted)
                    .filter(x -> x.contains(configElement.getValue()))
                    .findFirst()
                    .ifPresent(element -> map.put(
                            configElement.getKey(),
                            StringHandling.extract(element, configElement.getValue(), false)));
        }
        return map;
    }

    private List<MessagePart> excludeAttachment(MessagePart messagePart) {
        if (messagePart.getParts() != null) {
            return messagePart.getParts()
                    .stream()
                    .filter(m -> m.getMimeType().equals("text/html") || m.getMimeType().equals("text/plain"))
                    .collect(Collectors.toList());
        }
        return null;
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
        for (MessagePart messagePart: messageParts) {
//               System.out.println(messagePart);
            if (messagePart.getMimeType().equals("text/plain")) {
//                   System.out.println(messagePart.getBody().getData());
                sb.append(messagePart.getBody().getData());
            }
            // use recursive meth to get all parts content text/plain
            if (messagePart.getParts() != null) {
                getPlainTextFromMessageParts(messagePart.getParts(), sb);
            }
        }
    }

    private String getHeaderValueFromMessage(MessagePart messagePart, String value) {

        List<MessagePartHeader> headers = messagePart.getHeaders();
        for (MessagePartHeader header: headers) {
            if (header.getName().equals(value)) {
                return header.getValue().trim();
            }
        }
        return null;
    }

    public void lastMessageTimeStamp() throws IOException, ParseException {
        LogUtil.lastMessageTimeStamp();
    }


}
