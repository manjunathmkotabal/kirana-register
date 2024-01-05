package com.example.kirana.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling health check for the Kirana application.
 */
@RestController
@RequestMapping("/")
public class KiranaController {
    /**
     * Endpoint to check the health status of the application.
     *
     * @return A string indicating the health status of the application.
     */
    @GetMapping("/health-check")
    public String HealthCheck(){
        return "App is healthy";
    }
}

