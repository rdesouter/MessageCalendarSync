package com.rdesouter.service;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.rdesouter.SyncAbstract;
import com.rdesouter.model.MessageConstant;
import com.rdesouter.model.MessageMap;
import com.rdesouter.model.SyncEvent;
import com.rdesouter.model.SyncMessage;
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
import java.util.*;

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
        ListMessagesResponse messageList = gmail
                .users().messages().list("me")
                .setQ("label:dev@papymousse.be")
                .setMaxResults(5L).execute();
        List<Message> messages = messageList.getMessages();
        List<SyncMessage> syncMessages = new ArrayList<>();

        if (messageList.isEmpty()) {
            System.out.println("No message found");
        } else {
            System.out.println("Message found:");

            for (Message message: messages) {

                message = gmail.users().messages().get("me", message.getId()).setFormat("FULL").execute();
                MessagePart messagePart = message.getPayload();

                if (messagePart != null) {
                    String subject = "";
                    subject = getSubjectMessage(messagePart, subject);

                    //TODO REFACTOR:
                    // before add to list check if
                    // there's multipart and check for each part wich is the Content-type
                    // html/text - plain/text - multipart/mixed - etc
                    // after that extract information from payload
                    syncMessages.add(createSyncMessageBasedOn(message));
                }
            }
        }
        return syncMessages;
    }

    private SyncMessage createSyncMessageBasedOn(Message message) throws IOException {

        StringBuilder sb = new StringBuilder();

        if (message.getPayload().getParts() == null){
            String messageBody = new String(Base64.decodeBase64(message.getPayload().getBody().getData()), StandardCharsets.UTF_8);
            System.out.println("utf8 decoded body without parts: \n " + messageBody);
            //TODO need to extract from not part message as well

            //Harcoded just for first test
            SyncEvent syncEvent = new SyncEvent("firstEvent", "rendez-vous pris via l'API");
            SyncMessage syncMessage1 = new SyncMessage(message.getPayload().getBody().getData(), syncEvent);

            return syncMessage1;

        }else {
            getPlainTextFromMessageParts(message.getPayload().getParts(), sb);
            System.out.println("base64 bodyparts: " + sb);
            byte[] bodyBytes = Base64.decodeBase64(sb.toString());
            String messageBody = new String(bodyBytes, StandardCharsets.UTF_8);
            System.out.println("utf8 decoded bodyparts: \n"+ messageBody);

            String[] contentSplitted = StringHandling.splitNewLine(messageBody);
            HashMap<String,String> map = extractValueFromMessageBody(contentSplitted);

            if(map.isEmpty()){
                LOGGER.warn("message contains no element for create event \n" + sb);
                //TODO throw custom exception
                return new SyncMessage(sb.toString());
            }else {
                LOGGER.info("value extracted from message body:" + map);
                return new SyncMessage(sb.toString(), new SyncEvent("tempId", map.get("address")));
            }
        }
    }

    private HashMap<String,String> extractValueFromMessageBody(String[] contentSplitted) throws IOException {

        HashMap<String, String> mapForCreateEvent = new HashMap<>();
        MessageMap messageConfMapped = SyncMessageUtil.getMessageConfMapped();

        for (Map.Entry<String, String> e: messageConfMapped.messageMap.entrySet()){
            Arrays.stream(contentSplitted)
                    .filter(x -> x.contains(e.getValue()))
                    .findFirst()
                    .ifPresent(x -> mapForCreateEvent.put(e.getKey(), StringHandling.extract(x, e.getValue(),false)));
        }
        return mapForCreateEvent;
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

    private String getSubjectMessage(MessagePart messagePart, String subject) {

        List<MessagePartHeader> headers = messagePart.getHeaders();
        for (MessagePartHeader header: headers) {
            if (header.getName().equals("Subject")) {
                return header.getValue().trim();
            }
        }
        return subject;
    }

    public void lastMessageTimeStamp() throws IOException, ParseException {
        LogUtil.lastMessageTimeStamp();
    }


}
