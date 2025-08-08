package com.peter.wwii_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public String homeControllerHandler() {
        return "Welcome to my WWII web application, where you can learn a lot about the biggest armed conflict of the XX century!";
    }
}
