package com.pacoprojects.service;

import com.pacoprojects.model.ApplicationUser;
import com.pacoprojects.model.TokenConfirmation;
import com.pacoprojects.repository.ApplicationUserRepository;
import com.pacoprojects.repository.TokenConfirmationRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TokenConfirmationService {

    private final TokenConfirmationRepository tokenConfirmationRepository;
    private final ApplicationUserRepository applicationUserRepository;

    public TokenConfirmation generateToken(ApplicationUser applicationUser) {

        String token = UUID.randomUUID().toString();

        return new TokenConfirmation(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                applicationUser);
    }


    public void saveTokenConfirmation(TokenConfirmation token) {
        tokenConfirmationRepository.save(token);
    }

    @Transactional
    public ResponseEntity<String> confirmToken(String token) {

        /* Verifica se Token vindo por parametro URL realmente existe no Banco de Dados*/
        TokenConfirmation tokenConfirmation = tokenConfirmationRepository.findTokenConfirmationByToken(token)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token do not exist");
                });

        /* Apos Token verificar existencia no Banco, Verifica se Token JA NAO esta confirmado com status getConfirmedAt */
        if (tokenConfirmation.getConfirmedAt() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already confirmed");
        }

        /* Se Token existe e nao esta ainda confirmado, Verifica se Token nao esta expirado 15 minutos */
        if (tokenConfirmation.getExpiredAt().isBefore(LocalDateTime.now())) {
//            TODO: RE-SEND new token
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token is expired");
        }

        /* Se token existe, NAO esta confirmado E NAO esta expirado. Seta confirmacao para Data e Hora atual*/
        tokenConfirmation.setConfirmedAt(LocalDateTime.now());

        /* Troca status de Usuario para Enabled = true (Usuario Habilitado a logar no sistema) */
        enableApplicationUser(tokenConfirmation.getApplicationUser().getEmail());
        return ResponseEntity.ok("User is now enable");
    }

    /* Metodo que busca usuario no Banco por email e seta Usuario habilitado para logar no sistema */
    public void enableApplicationUser(String email) {
        ApplicationUser user = applicationUserRepository.findApplicationUserByEmail(email).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        });
        user.setEnabled(true);
    }
}
