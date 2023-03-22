package com.pacoprojects.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/welcome")
public class WelcomeController {

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Ola Mundo da API");
    }

    @GetMapping(path = "goodbye")
    public ResponseEntity<String> sayGoodBye() {
        return ResponseEntity.ok("Adeus, fuis");
    }

}
