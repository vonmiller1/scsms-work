package com.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    // Create a logger instance
    // private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/health")
    public String healthCheck() {
        // Log the health check message
        // log.info("Health is ok!");
        return "Application is running!";
    }
}
