package com.pacoprojects.repository;

import com.pacoprojects.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    Optional<ApplicationUser> findApplicationUserByEmail(String email);

    boolean existsApplicationUserByEmail(String email);
}
