package com.learn.salesservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/sales/ping")
public class PingController {

    // Injecting the service name from the application.properties file
    @Value("${spring.application.name}")
    private String serviceName;

    // This endpoint will return the service name along with a Pong message
    @GetMapping("")
    public String ping() {
        return "Pong from " + serviceName;
    }
}
