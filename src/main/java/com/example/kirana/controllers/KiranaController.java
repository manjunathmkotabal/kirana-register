package com.example.kirana.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class KiranaController {
    @GetMapping("/health-check")
    public String HealthCheck(){
        return "App is healthy";
    }
}

