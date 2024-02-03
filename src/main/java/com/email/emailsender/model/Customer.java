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
        this.threshold = (System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(threshold));
        this.status = status;
        this.emailSent = emailSent;
    }

    // Check if the condition is met for sending an email
    public boolean shouldSendEmail() {
        return !emailSent && !status && (System.currentTimeMillis() >= threshold);
    }
}
