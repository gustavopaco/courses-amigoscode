package com.pacoprojects.service;

import com.pacoprojects.model.ApplicationUser;
import com.pacoprojects.model.TokenConfirmation;
import com.pacoprojects.repository.ApplicationUserRepository;
import com.pacoprojects.security.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
public class ApplicationUserService implements UserDetailsService {

    private final static String USER_DO_NOT_EXIST_MESSAGE = "User %s do not exist, try to register yourself or contact an admin.";
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenConfirmationService tokenConfirmationService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return applicationUserRepository.findApplicationUserByEmail(email)
                .orElseThrow(() -> {throw new UsernameNotFoundException(String.format(USER_DO_NOT_EXIST_MESSAGE, email));});
    }

    public String signUpApplicationUser(ApplicationUser applicationUser) {

        try {
            boolean existsApplicationUserByEmail = applicationUserRepository.existsApplicationUserByEmail(applicationUser.getEmail());

            if (existsApplicationUserByEmail) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already taken");
            }

            String passwordEncoded = passwordEncoder.encode().encode(applicationUser.getPassword());
            applicationUser.setPassword(passwordEncoded);
            ApplicationUser userCreated = applicationUserRepository.save(applicationUser);

//        TODO: Send confirmation Token
            TokenConfirmation generatedToken = tokenConfirmationService.generateToken(userCreated);
            tokenConfirmationService.saveTokenConfirmation(generatedToken);

//        TODO: Send Email

            /* Retornando token gerado */
            return generatedToken.getToken();
        } catch (Exception e) {
            return "Failed to register new User.";
        }
    }
}
