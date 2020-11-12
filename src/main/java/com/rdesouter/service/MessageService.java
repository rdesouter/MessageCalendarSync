package com.rdesouter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.rdesouter.utils.StringHandling;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {


    public void getMessages(Gmail gmailService) throws IOException {
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
            MessageMap messageMap = getMessageMap();

            for (Message message: messages) {
                message = gmailService.users().messages().get("me", message.getId()).setFormat("FULL").execute();
                MessagePart messagePart = message.getPayload();

                String subject = "";
                if (messagePart != null) {
                    subject = getSubjectMessage(messagePart, subject);
                    getMapForCreateEvent(messageMap, message);

                }
            }
        }
    }

    public String getSubjectMessage(MessagePart messagePart, String subject) {

        List<MessagePartHeader> headers = messagePart.getHeaders();
        for (MessagePartHeader header: headers) {
            //find the subject header
            if (header.getName().equals("Subject")) {
                return header.getValue().trim();
            }
        }
        return subject;
    }

    public void getMapForCreateEvent(MessageMap messageMap, Message message) {

        StringBuilder sb = new StringBuilder();
        HashMap<String, String> mapForCreateEvent = new HashMap<>();

        if (message.getPayload().getParts() == null){
            String messageBody = new String(Base64.decodeBase64(message.getPayload().getBody().getData()), StandardCharsets.UTF_8);
            System.out.println("utf8 decoded body without parts: \n " + messageBody);
            //TODO need to extract from not part message as well

        }else {
            getPlainTextFromMessageParts(message.getPayload().getParts(), sb);
            System.out.println("base64 bodyparts: " + sb);
            byte[] bodyBytes = Base64.decodeBase64(sb.toString());
            String messageBody = new String(bodyBytes, StandardCharsets.UTF_8);
            System.out.println("utf8 decoded bodyparts: \n"+ messageBody);

            String[] contentSplitted = StringHandling.splitNewLine(messageBody);
            extractValueFromMessageBody(contentSplitted, messageMap,mapForCreateEvent);

        }
    }

    private static MessageMap getMessageMap() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        MessageMap messageMapped = mapper.readValue(new File(cl.getResource("messageConf.yml").getFile()), MessageMap.class);
        return messageMapped;
    }

    private static HashMap<String,String> extractValueFromMessageBody(String[] contentSplitted, MessageMap messageMap, HashMap<String,String> mapForCreateEvent) {
        for (Map.Entry<String, String> e: messageMap.messageMap.entrySet()){
            Arrays.stream(contentSplitted)
                    .filter(x -> x.contains(e.getValue()))
                    .findFirst()
                    .ifPresent(x -> mapForCreateEvent.put(e.getKey(),StringHandling.extract(x, e.getValue(),false)));
        }
        return mapForCreateEvent;
    }

    public void getPlainTextFromMessageParts(List<MessagePart> messageParts, StringBuilder sb) {
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

    public void getHtmlTextFromMessageParts(List<MessagePart> messageParts, StringBuilder sb) {
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

}
