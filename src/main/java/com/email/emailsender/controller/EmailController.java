package com.email.emailsender.controller;

import com.email.emailsender.model.Customer;
import com.email.emailsender.service.NotificationService;
import com.email.emailsender.service.Scheduler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/email-notification")
public class EmailController {
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody List<Customer> customers) {
        // Define the callback logic
        Scheduler.EmailSentCallback callback = emailAddress -> {
            // Handle the successful email sent notification
            System.out.println("Email sent successfully to: " + emailAddress);

            // You can add more logic here if needed
        };

        // Start the scheduler for each customer
        for (Customer customer : customers) {
            Scheduler.scheduler(customer, callback);
        }

        // Return a response indicating that the scheduling has started
        System.out.println("Email scheduling started");
        return new ResponseEntity<>("Email scheduling started", HttpStatus.OK);
    }

}
