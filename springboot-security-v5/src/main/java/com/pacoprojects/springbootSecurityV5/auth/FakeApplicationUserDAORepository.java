package com.pacoprojects.springbootSecurityV5.auth;

import com.pacoprojects.springbootSecurityV5.security.ApplicationUserRole;
import com.pacoprojects.springbootSecurityV5.security.PasswordConfig;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*  REPOSITORY - USUARIO_REPOSITORY|  */
@Repository(value = "fakeRepository")
public class FakeApplicationUserDAORepository implements ApplicationUserDAO{

    private final PasswordConfig passwordConfig;

    public FakeApplicationUserDAORepository(PasswordConfig passwordConfig) {
        this.passwordConfig = passwordConfig;
    }

    /* Metodo que MAPEIA lista de usuarios na MEMORIA e busca por Usuario pelo username */
    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers().stream()
                .filter(applicationUser -> applicationUser.getUsername().equals(username))
                .findFirst();
    }

    /* Metodo que Cria usuarios na memoria*/
    private List<ApplicationUser> getApplicationUsers() {
        List<ApplicationUser> applicationUsers = List.of(
                new ApplicationUser(
                        "augusto",
                        passwordConfig.passwordEncoder().encode("123"),
                        ApplicationUserRole.STUDENT.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true),
                new ApplicationUser(
                        "gustavo",
                        passwordConfig.passwordEncoder().encode("123"),
                        ApplicationUserRole.ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true),
                new ApplicationUser(
                        "tom",
                        passwordConfig.passwordEncoder().encode("123"),
                        ApplicationUserRole.ADMIN_TRAINEE.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true)
        );
        return applicationUsers;
    }
}
