package com.rdesouter.service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.rdesouter.SyncAbstract;
import com.rdesouter.dao.repository.MessageRepo;
import com.rdesouter.dao.repository.MessageRepoCustom;
import com.rdesouter.dto.SyncMessageDto;
import com.rdesouter.dto.UserDto;
import com.rdesouter.model.*;
import com.rdesouter.utils.*;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SyncMessageService extends SyncAbstract implements MessageConstant {

    @Autowired
    private SyncEventService syncEventService;
    @Autowired
    private Gmail gmail;
    @Autowired
    private AppPropertiesValues appPropertiesValues;
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private MessageRepoCustom messageRepoCustom;

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncMessageService.class);
    private static final MessageConfig MESSAGE_CONFIG = SyncMessageUtil.getMessageConfigMapped();

    public Message sendMail(String messageBody) throws MessagingException, IOException {
        MimeMessage messageContent = SyncMessageUtil.getMimeMessage(SENDER, RECEIVER, SUBJECT, messageBody);
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
    public List<SyncMessageDto> getMessages(User connectedUser) throws IOException {
        //TODO set setQ and MaxResult in param of param for user store in DB
        // of set only setQ with label and timestamp
        ListMessagesResponse messagesResponse = gmail.users()
                .messages().list("me")
                .setQ("label:dev@papymousse.be")
                .setMaxResults(10L).execute();
        if (!messagesResponse.isEmpty()) {
            List<SyncMessage> syncMessages = new ArrayList<>();
            for (Message message: messagesResponse.getMessages()) {
                message = gmail.users()
                        .messages()
                        .get("me", message.getId())
                        .setFormat("FULL")
                        .execute();
                SyncMessage synced = messageRepoCustom.findById(message.getId());
                if (synced == null){
                    PreparedSyncMessage preparedSyncMessage = getPreparedSyncMessage(message);
                    syncMessages.add(saveMessage(connectedUser, preparedSyncMessage));
                }
            }
            return messageRepo.findAll().stream().map(this::convert).collect(Collectors.toList());
        } else {
            LOGGER.debug("no message was found: " + messagesResponse.getMessages());
            return null;
        }
//        Harcoded just for first test TO DELETE
//        SyncEvent syncEvent = new SyncEvent("firstEvent", "rendez-vous pris via l'API");
//        SyncMessage syncMessage1 = new SyncMessage(message.getPayload().getBody().getData(), syncEvent);
    }

    private PreparedSyncMessage getPreparedSyncMessage(Message message) {
        PreparedSyncMessage preparedSyncMessage = new PreparedSyncMessage();
        MessagePart messagePart = message.getPayload();
        Optional<List<MessagePart>> isMultiPart = Optional.ofNullable(messagePart.getParts());
        preparedSyncMessage
                .setMessage(message)
                .setConfig(MESSAGE_CONFIG)
                .setHeaders(createMessageHeaders(messagePart));
        if (isMultiPart.isPresent()){
            preparedSyncMessage
                    .setParts(excludeAttachment(messagePart))
                    .setExtracted(getValueExtracted(preparedSyncMessage.getParts(),MESSAGE_CONFIG));
        }else {
            preparedSyncMessage
                    .setParts(Collections.singletonList(messagePart))
                    .setExtracted(getValueExtracted(messagePart,MESSAGE_CONFIG));
        }
        return preparedSyncMessage;
    }

    private SyncMessage saveMessage(User connectedUser, PreparedSyncMessage preparedSyncMessage) {
        if (preparedSyncMessage.getExtraced().size() > 0
                && preparedSyncMessage.getExtraced().size() < preparedSyncMessage.getConfig().map.size()) {
            // only store subject, messageFrom, sendingDate + payload but not create event
            SyncMessage partialSyncMessage = new SyncMessage(
                    preparedSyncMessage.getMessage().getId(),
                    createMessagePortion(
                            preparedSyncMessage.getParts(),
                            preparedSyncMessage.getHeaders()),
                    createPartialSyncEvent(
                            preparedSyncMessage.getConfig(),
                            preparedSyncMessage.getExtraced()),
                    connectedUser);
            messageRepo.save(partialSyncMessage);
            return partialSyncMessage;

        } else if (preparedSyncMessage.getExtraced().size() == 0) {
            // store subject, comingFrom, date but not payload and not create event
            SyncMessage notSyncMessage = new SyncMessage(
                    preparedSyncMessage.getMessage().getId(),
                    createMessagePortionWithEmptyMessageBody(preparedSyncMessage.getHeaders()),
                    new SyncEvent(UUID.randomUUID().toString()),
                    connectedUser);
            messageRepo.save(notSyncMessage);
            return notSyncMessage;

        } else if (preparedSyncMessage.getExtraced().size() == preparedSyncMessage.getConfig().map.size()) {
            String patternDate = appPropertiesValues.getConfigValue("pattern.date");
            String patternTime = appPropertiesValues.getConfigValue("pattern.hour");
            // store data and create event
            SyncMessage fullySyncMessage = new SyncMessage(
                    preparedSyncMessage.getMessage().getId(),
                    createMessagePortion(
                            preparedSyncMessage.getParts(),
                            preparedSyncMessage.getHeaders()),
                    createFullySyncEvent(
                            preparedSyncMessage.getHeaders(),
                            preparedSyncMessage.getExtraced(),
                            patternDate,
                            patternTime),
                    connectedUser);
            messageRepo.save(fullySyncMessage);
            return fullySyncMessage;
        }
        return null;
    }

    private SyncEvent createPartialSyncEvent(
            MessageConfig messageConfig,
            HashMap<String, String> extracted) {
        return new SyncEvent(
                UUID.randomUUID().toString(),
                findMissingsKeys(extracted, messageConfig.map));
    }

    private SyncEvent createFullySyncEvent(
            MessageHeaders messageHeaders,
            HashMap<String, String> extracted,
            String patternDate,
            String patternTime) {
        return new SyncEvent(
                UUID.randomUUID().toString(),
                messageHeaders.getSubject(),
                extracted.get("address"),
                DateHandling.transformDateStringForEvent(
                        patternDate,
                        patternTime,
                        extracted.get("beginDate"),
                        extracted.get("beginHour")),
                DateHandling.transformDateStringForEvent(
                        patternDate,
                        patternTime,
                        extracted.get("endDate"),
                        extracted.get("endHour")),
                Arrays.asList(
                        messageHeaders.getFrom(),
                        messageHeaders.getTo()),
                "extra-info:" + extracted.get("phone"));
    }

    private MessageHeaders createMessageHeaders(MessagePart part) {
        return new MessageHeaders(
                getHeaderValueFromMessage(part, HEADERS_SUBJECT),
                getHeaderValueFromMessage(part, HEADERS_SENDING_DATE),
                getHeaderValueFromMessage(part,HEADERS_FROM),
                getHeaderValueFromMessage(part, HEADERS_TO)
        );
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

    private MessagePortion createMessagePortionWithEmptyMessageBody(MessageHeaders headers) {
        return new MessagePortion(
                headers.getSubject(),
                headers.getFrom(),
                headers.getSendingDate(),
                Collections.singletonList(new MessageBody()));
    }

    private MessagePortion createMessagePortion(List<MessagePart> parts, MessageHeaders headers) {
        List<MessageBody> messageBodies = new ArrayList<>();
        parts.forEach(part -> messageBodies.add(
                        new MessageBody(part.getBody().getData(), part.getMimeType())
        ));
        return new MessagePortion(
                headers.getSubject(),
                headers.getFrom(),
                headers.getSendingDate(),
                messageBodies);
    }

    private List<String> findMissingsKeys(HashMap<String,String> first, HashMap<String,String> hasAll){
        List<String> missingKeysFromMessage = new ArrayList<>();
        HashSet<String> unionKeys = new HashSet<>(first.keySet());

        unionKeys.addAll(hasAll.keySet());
        unionKeys.removeAll(first.keySet());
        unionKeys.forEach(e -> missingKeysFromMessage.add(hasAll.get(e)));
        return missingKeysFromMessage;
    }

    private HashMap<String,String> getValueExtracted(MessagePart part, MessageConfig messageConfig) {
        byte[] bodyBytes = part.getBody().decodeData();
        String messageBody = new String(bodyBytes, StandardCharsets.UTF_8);
        String[] contentSplitted = StringHandling.splitNewLine(messageBody);
        return extractValueFromMessageBody(contentSplitted, messageConfig);
    }

    private HashMap<String,String> getValueExtracted(List<MessagePart> parts, MessageConfig messageConfig){
        for(MessagePart part: parts) {
            byte[] bodyBytes = part.getBody().decodeData();
            String messageBody = new String(bodyBytes, StandardCharsets.UTF_8);
            String[] contentSplitted = StringHandling.splitNewLine(messageBody);
            return extractValueFromMessageBody(contentSplitted, messageConfig);
        }
        return null;
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

    private SyncMessageDto convert(SyncMessage syncMessage) {
        return new SyncMessageDto(
                syncMessage.getPortion(),
                syncMessage.getSyncEvent(),
                new UserDto(
                        syncMessage.getUser().getLogin(),
                        syncMessage.getUser().getRole())
        );
    }

    public void lastMessageTimeStamp() throws IOException, ParseException {
        LogUtil.lastMessageTimeStamp();
    }
    //Unused method for moment
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


}
