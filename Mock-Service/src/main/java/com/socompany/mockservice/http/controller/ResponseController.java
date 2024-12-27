package com.socompany.mockservice.http.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/mock")
public class ResponseController {
    
    @GetMapping("/get200")
    public ResponseEntity<String> get200() {

        return ResponseEntity.ok("Success");
    }

    @GetMapping("/get500")
    public ResponseEntity<Object> get500() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("[Mock] - Iternal Server Error");
    }

    @PostMapping
    public ResponseEntity<Object> post() {
        return ResponseEntity.ok("Recieved post method in Mock Controller");
    }
}
