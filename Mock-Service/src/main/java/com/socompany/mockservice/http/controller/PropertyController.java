package com.socompany.mockservice.http.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/api/mock/")
public class PropertyController {

    @Value("${my.greeting}")
    private String message;

    @GetMapping("/get/message")
    public ResponseEntity<String> getProperty() {
        return ResponseEntity.ok("Message: " + (message.isEmpty() ? "There is no msg" : message + "\n"));
    }


}
