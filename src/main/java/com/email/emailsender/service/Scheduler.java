package com.email.emailsender.service;

import com.email.emailsender.model.Customer;

import javax.mail.MessagingException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    public static void scheduler(Customer customer, EmailSentCallback callback) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


        int initialDelay = 0;
        int periodA = 30;
        // Schedule to update status of customer for db simulation every 30 seconds
        scheduler.scheduleAtFixedRate(customer::updateStatusFromDatabase, initialDelay, periodA, TimeUnit.SECONDS);

        // Schedule to check if threshold reached or status changed to send specified email
        int periodB = 10;
        scheduler.scheduleAtFixedRate(() -> {
            //System.out.println("CURRENT: " + System.currentTimeMillis());
            //System.out.println(customer.getEmail() + " " + customer.isStatus() + " " + customer.getThreshold());
            if (customer.shouldSendLoadedEmail() || customer.shouldSendEmail()) {
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
        }, initialDelay, periodB, TimeUnit.SECONDS);
    }

    // Callback interface for email sent notification
    public interface EmailSentCallback {
        void onEmailSent(String emailAddress);
    }


    public boolean changeStatusTimeCheck(Customer customer, Long minutesBefore){
        long currentTime = System.currentTimeMillis();
        Long minutesBeforeThreshold = customer.getThreshold() - TimeUnit.MINUTES.toMillis(minutesBefore);
        Long minutesAfterChange = minutesBeforeThreshold + TimeUnit.MINUTES.toMillis(1);

        return currentTime >= minutesBeforeThreshold && currentTime <= minutesAfterChange;
    }
}
