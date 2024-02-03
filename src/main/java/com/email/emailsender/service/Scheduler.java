package com.email.emailsender.service;

import com.email.emailsender.model.Customer;

import javax.mail.MessagingException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    public static void scheduler(Customer customer, EmailSentCallback callback) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Schedule the task to run every minute
        int initialDelay = 0;
        int period = 10;
        scheduler.scheduleAtFixedRate(() -> {
            if (customer.shouldSendEmail()) {
                boolean emailSent = false;
                try {
                    emailSent = NotificationService.sendEmail(customer);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
                if (emailSent) {
                    // Invoke the callback when the email is sent successfully
                    callback.onEmailSent(customer.getEmail());
                }
            }
        }, initialDelay, period, TimeUnit.SECONDS);
    }

    // Callback interface for email sent notification
    public interface EmailSentCallback {
        void onEmailSent(String emailAddress);
    }
}
