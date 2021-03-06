package com.rdesouter.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.model.Message;
import com.rdesouter.model.MessageConstant;
import com.rdesouter.model.MessageConfig;
import org.springframework.security.authentication.AuthenticationManager;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import static javax.mail.Message.RecipientType.TO;

public class SyncMessageUtil implements MessageConstant {


    public static MessageConfig getMessageConfigMapped() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            File conf = new File(Objects.requireNonNull(cl.getResource("messageConfig.yml")).getFile());
            return mapper.readValue(conf, MessageConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MimeMessage getMimeMessageWithHtmlPart(String to, String from, String sub, String bodyText) throws MessagingException {
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

    public static MimeMessage getMimeMessage (String to, String from, String subject, String bodyText) throws MessagingException {
        Session session = Session.getDefaultInstance(new Properties(), null);
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(from));
        message.addRecipient(TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(bodyText);

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
