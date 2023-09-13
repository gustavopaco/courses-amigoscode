package com.pacoprojects.springbootSecurityV5.auth;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ApplicationUserDAO {

    Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}
