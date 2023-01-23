package com.pacoprojects.service;

import com.pacoprojects.model.ApplicationUser;
import com.pacoprojects.model.ApplicationUserRole;
import com.pacoprojects.model.UserRequest;
import com.pacoprojects.util.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class UserRequestService {

    private final EmailValidator emailValidator;
    private final ApplicationUserService applicationUserService;

    public ResponseEntity<String> register(UserRequest userRequest) {

        /* VALIDATE REGEX */
        boolean isValidEmail = emailValidator.test(userRequest.getEmail());

        if (!isValidEmail) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not valid");
        }

        String result = applicationUserService.signUpApplicationUser(
                new ApplicationUser(
                        userRequest.getFirstName(),
                        userRequest.getLastName(),
                        userRequest.getEmail(),
                        userRequest.getPassword(),
                        ApplicationUserRole.USER));
        return ResponseEntity.ok(result);
    }
}
