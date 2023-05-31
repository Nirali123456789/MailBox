package com.aiemail.superemail.Activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.aiemail.superemail.R;


import org.apache.commons.codec.binary.Base64;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Sample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String rawMessage ="Your job alert\n" +
                "                                                                                                            \n" +
                "                                                                                                    30+ new jobs match your preferences.  \n" +
                "                                                                                                              \n" +
                "                                                                                                    Stage financement de l'innovation secteur chimie biologie agro F/H\n" +
                "                                                                                                    Deloitte\n" +
                "                                                                                                    Paris\n" +
                "                                                                                                    2 connections\n" +
                "                                                                                                    View job:\n" +
                "                                                                                                          https://www.linkedin.com/comm/jobs/view/3573080120/?trackingId=VrqijS9E0OrtYKo2IJFjJQ%3D%3D&refId=4dc60fa5-efca-4c2c-b9a6-dd21e95da170&lipi=urn%3Ali%3Apage%3Aemail_email_job_alert_digest_01%3BSn2%2FdinURTCid6%2Byya50Hg%3D%3D&midToken=AQH6pC7w_83HFw&midSig=2wNDd6eMH0gqM1&trk=eml-email_job_alert_digest_01-job_card-0-view_job&trkEmail=eml-email_job_alert_digest_01-job_card-0-view_job-null-6lel40~lhluwffb~cz-null-null&eid=6lel40-lhluwffb-cz\n" +
                "                                                                                                    \n" +
                "                                                                                                    ---------------------------------------------------------\n" +
                "                                                                                                      \n" +
                "                                                                                                              \n" +
                "                                                                                                    Software Developer, Deloitte Global Technology (GS-Tech Solutions)\n" +
                "                                                                                                    Deloitte\n" +
                "                                                                                                    Toronto, ON\n" +
                "                                                                                                    2 connections\n" +
                "                                                                                                    View job:\n" +
                "                                                                                                          https://www.linkedin.com/comm/jobs/view/3520239697/?trackingId=64RCiVufRT0Vtvhqv%2BWb7g%3D%3D&refId=4dc60fa5-efca-4c2c-b9a6-dd21e95da170&lipi=urn%3Ali%3Apage%3Aemail_email_job_alert_digest_01%3BSn2%2FdinURTCid6%2Byya50Hg%3D%3D&midToken=AQH6pC7w_83HFw&midSig=2wNDd6eMH0gqM1&trk=eml-email_job_alert_digest_01-job_card-0-view_job&trkEmail=eml-email_job_alert_digest_01-job_card-0-view_job-null-6lel40~lhluwffb~cz-null-null&eid=6lel40-lhluwffb-cz\n" +
                "                                                                                                    \n" +
                "                                                                                                    ---------------------------------------------------------\n" +
                "                                                                                                      \n" +
                "                                                                                                              \n" +
                "                                                                                                    Manager/Senior Manager Audit F/H\n" +
                "                                                                                                    Deloitte\n" +
                "                                                                                                    Paris\n" +
                "                                                                                                    2 connections\n" +
                "                                                                                                    View job:\n" +
                "                                                                                                          https://www.linkedin.com/comm/jobs/view/3545725266/?trackingId=sPOUwGDDkUWDjox4%2BzOc5Q%3D%3D&refId=4dc60fa5-efca-4c2c-b9a6-dd21e95da170&lipi=urn%3Ali%3Apage%3Aemail_email_job_alert_digest_01%3BSn2%2FdinURTCid6%2Byya50Hg%3D%3D&midToken=AQH6pC7w_83HFw&midSig=2wNDd6eMH0gqM1&trk=eml-email_job_alert_digest_01-job_card-0-view_job&trkEmail=eml-email_job_alert_digest_01-job_card-0-view_job-null-6lel40~lhluwffb~cz-null-null&eid=6lel40-lhluwffb-cz\n" +
                "                                                                                                    \n" +
                "                                                                                                    ---------------------------------------------------------\n" +
                "                                                                                                      \n" +
                "                                                                                                              \n" +
                "                                                                                                    Categorie Protette Art. 1/18 Legge 68/99 – Junior - DEVELOPER\n" +
                "                                                                                                    Deloitte\n" +
                "                                                                                                    Vicenza\n" +
                "                                                                                                    2 connections\n" +
                "                                                                                                    View job:\n" +
                "                                                                                                          https://www.linkedin.com/comm/jobs/view/3576206510/?trackingId=wgKeV12t9IpY73r124aSng%3D%3D&refId=4dc60fa5-efca-4c2c-b9a6-dd21e95da170&lipi=urn%3Ali%3Apage%3Aemail_email_job_alert_digest_01%3BSn2%2FdinURTCid6%2Byya50Hg%3D%3D&midToken=AQH6pC7w_83HFw&midSig=2wNDd6eMH0gqM1&trk=eml-email_job_alert_digest_01-job_card-0-view_job&trkEmail=eml-email_job_alert_digest_01-job_card-0-view_job-null-6lel40~lhluwffb~cz-null-null&eid=6lel40-lhluwffb-cz\n" +
                "                                                                                                    \n" +
                "                                                                                                    ---------------------------------------------------------\n" +
                "                                                                                                      \n" +
                "                                                                                                              \n" +
                "                                                                                                    RA - Manager/Senior Manager - IT & Specialized Assurance - Corporate, Private & Public Sector - Accounting & Internal Controls\n" +
                "                                                                                                    Deloitte\n" +
                "                                                                                                    Italy\n" +
                "                                                                                                    2 connections\n" +
                "                                                                                                    View job:\n" +
                "                                                                                                          https://www.linkedin.com/comm/jobs/view/3575910254/?trackingId=SXaS5RkrBKlIonieL4sysg%3D%3D&refId=4dc60fa5-efca-4c2c-b9a6-dd21e95da170&lipi=urn%3Ali%3Apage%3Aemail_email_job_alert_digest_01%3BSn2%2FdinURTCid6%2Byya50Hg%3D%3D&midToken=AQH6pC7w_83HFw&midSig=2wNDd6eMH0gqM1&trk=eml-email_job_alert_digest_01-job_card-0-view_job&trkEmail=eml-email_job_alert_digest_01-job_card-0-view_job-null-6lel40~lhluwffb~cz-null-null&eid=6lel40-lhluwffb-cz\n" +
                "                                                                                                    \n" +
                "                                                                                                    ---------------------------------------------------------\n" +
                "                                                                                                      \n" +
                "                                                                                                              \n" +
                "                                                                                                    Mobile Developer\n" +
                "                                                                                                    Deloitte\n" +
                "                                                                                                    Seville\n" +
                "                                                                                                    2 connections\n" +
                "                                                                                                    View job:\n" +
                "                                                                                                          https://www.linkedin.com/comm/jobs/view/3579426642/?trackingId=nOxVZ1ym17ljTJXGShvFyQ%3D%3D&refId=4dc60fa5-efca-4c2c-b9a6-dd21e95da170&lipi=urn%3Ali%3Apage%3Aemail_email_job_alert_digest_01%3BSn2%2FdinURTCid6%2Byya50Hg%3D%3D&midToken=AQH6pC7w_83HFw&midSig=2wNDd6eMH0gqM1&trk=eml-email_job_alert_digest_01-job_card-0-view_job&trkEmail=eml-email_job_alert_digest_01-job_card-0-view_job-null-6lel40~lhluwffb~cz-null-null&eid=6lel40-lhluwffb-cz\n" +
                "                                                                                                    \n" +
                "                                                                                                    ---------------------------------------------------------\n" +
                "                                                                                                      \n" +
                "                                                                                                    See all jobs on LinkedIn: ";

        try {
            Session session = Session.getDefaultInstance(new Properties());
            MimeMessage message = new MimeMessage(session, new ByteArrayInputStream(rawMessage.getBytes()));

            processParts(message);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }

    private void processParts(Part part) throws IOException, MessagingException {
        if (part.isMimeType("text/plain")) {
            // Text part
            String text = part.getContent().toString();
            Log.d("processParts", "processParts: "+text);
            // Process the text
        } else if (part.isMimeType("text/html")) {
            // HTML part
            String html = part.getContent().toString();
            // Process the HTML
            Log.d("processParts", "processParts: "+html);
        } else if (part.isMimeType("multipart/*")) {
            // Multipart part

            Multipart multipart = (Multipart) part.getContent();
            int count = multipart.getCount();
            Log.d("processParts", "processParts: "+multipart);

            for (int i = 0; i < count; i++) {
                processParts(multipart.getBodyPart(i));
            }
        } else if (part instanceof MimeBodyPart) {
            // Single body part
            MimeBodyPart bodyPart = (MimeBodyPart) part;

            if (bodyPart.isMimeType("image/*")) {
                // Image part
                String contentType = bodyPart.getContentType();
                String fileName = bodyPart.getFileName();
                String base64Content = Base64.encodeBase64String((byte[]) bodyPart.getContent());
                Log.d("processParts", "processParts: "+contentType+"???"+fileName+"??"+base64Content);

                // Process the image, using contentType, fileName, and base64Content
            }
        }
    }
}
