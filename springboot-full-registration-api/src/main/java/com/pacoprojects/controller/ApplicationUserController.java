package com.pacoprojects.controller;

import com.pacoprojects.model.UserRequest;
import com.pacoprojects.service.UserRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "app-user")
@AllArgsConstructor
public class ApplicationUserController {

    private final UserRequestService userRequestService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody UserRequest userRequest) {
        return userRequestService.register(userRequest);
    }
}
