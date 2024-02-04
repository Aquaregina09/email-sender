package com.email.emailsender.model;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class Customer {
    private String email;
    private Long threshold;
    private boolean status;
    private boolean emailSent;

    public Customer(String email, int threshold, boolean status, boolean emailSent) {
        this.email = email;
        //adds the threshold (in minutes) to the time the API is triggered
        this.threshold = (System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(threshold));
        this.status = status;
        this.emailSent = emailSent;
    }

    // Check if the condition is met for sending an email
    public boolean shouldSendEmail() {
        return !emailSent && !status && (System.currentTimeMillis() >= threshold);
    }

    // Check if the condition is met for sending an email
    public boolean shouldSendLoadedEmail(){
        return !emailSent && status;
    }

    public void updateStatusFromDatabase() {
        // Simulate fetching the latest status from a database
        boolean newStatus = fetchStatusFromDatabase();
        if (newStatus){
            setStatus(true);
        }
    }

    private boolean fetchStatusFromDatabase() {
        // Simulate fetching the status from a database
        return Math.random() < 0.2; // Simulate a random status change
    }

}
