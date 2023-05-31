package com.aiemail.superemail.feature.utilis;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class EmailConverter {

    public static String convertToPlainText(String rawEmailData) throws MessagingException, IOException {
        // Create properties and session
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        // Create a MimeMessage object from raw email data
        InputStream inputStream = new ByteArrayInputStream(rawEmailData.getBytes());
        MimeMessage message = new MimeMessage(session, inputStream);

        // Extract the plain text content
        String plainText = extractPlainText(message);

        return plainText;
    }

    private static String extractPlainText(Part part) throws MessagingException, IOException {
        if (part.isMimeType("text/plain")) {
            // Content is plain text
            return part.getContent().toString();
        } else if (part.isMimeType("multipart/*")) {
            // Content is multipart, handle each part recursively
            Multipart multipart = (Multipart) part.getContent();
            int count = multipart.getCount();
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String text = extractPlainText(bodyPart);
                if (text != null && !text.isEmpty()) {
                    return text;
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            // Content is another email message, handle it recursively
            return extractPlainText((Part) part.getContent());
        }

        return null;
    }
}
