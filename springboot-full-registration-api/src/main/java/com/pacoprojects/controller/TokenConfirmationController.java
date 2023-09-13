package com.pacoprojects.controller;

import com.pacoprojects.service.TokenConfirmationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "confirmation")
@AllArgsConstructor
public class TokenConfirmationController {

    private final TokenConfirmationService tokenConfirmationService;

    @GetMapping
    public ResponseEntity<String> confirmToken(@RequestParam(name = "token") String token) {
        return tokenConfirmationService.confirmToken(token);
    }
}
