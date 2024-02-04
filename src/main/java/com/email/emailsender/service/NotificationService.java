package com.email.emailsender.service;

import com.email.emailsender.model.Customer;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class NotificationService {
    public static boolean sendEmail(Customer customer) throws MessagingException {
        // Set your email credentials and properties
        final String username = "rampNotify@gmail.com";
        final String password = "azcn fuyc qxkx sbqm";
        String host = "smtp.gmail.com"; // Gmail SMTP host
        int port = 587; // Port for TLS

        // Create properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.protocols", "TLSv1.3");
        properties.put("mail.smtp.ssl.ciphersuites", "TLS_AES_256_GCM_SHA384");


        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        Session session = Session.getDefaultInstance(properties, authenticator);

        Transport transport = session.getTransport("smtp");
        transport.connect("smtp.gmail.com", username, password);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(customer.getEmail()));
            message.setSubject("Notification:Ramp Status Update");
            if (customer.shouldSendLoadedEmail()){
                message.setText("Dear Customer,\n\nYour shipment has been loaded to the aircraft");
            }
            else {
                message.setText("Dear Customer,\n\nYour shipment is not yet loaded, and the threshold has been reached.");
            }

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully!");
            customer.setEmailSent(true);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

    }
}
