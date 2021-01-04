package com.rdesouter.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.model.Message;
import com.rdesouter.model.MessageConstant;
import com.rdesouter.model.MessageMap;

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
import java.util.Properties;

import static javax.mail.Message.RecipientType.TO;

public class SyncMessageUtil implements MessageConstant {


    public static MessageMap getMessageConfMapped() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        MessageMap map = mapper.readValue(new File(cl.getResource("messageConf.yml").getFile()), MessageMap.class);
        return map;
    }


    public static MimeMessage createMessageWithMultiPart(String to, String from, String sub, String bodyText) throws MessagingException {
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

    public static Message createMessage(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}
