package com.pacoprojects.service;

import com.pacoprojects.email.details.EmailDetails;
import com.pacoprojects.email.details.EmailService;
import com.pacoprojects.model.ApplicationUser;
import com.pacoprojects.model.ApplicationUserRole;
import com.pacoprojects.model.UserRequest;
import com.pacoprojects.email.config.EmailBody;
import com.pacoprojects.email.sender.EmailSender;
import com.pacoprojects.util.EmailValidator;
import com.pacoprojects.email.generic.ObjetoJavaGMail;
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
    private final EmailSender emailSender;
    private final EmailBody emailBody;
    private final ObjetoJavaGMail objetoJavaGMail;

    private final EmailService emailService;

    public ResponseEntity<String> register(UserRequest userRequest) {

        /* VALIDATE REGEX */
        boolean isValidEmail = emailValidator.test(userRequest.getEmail());

        if (!isValidEmail) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not valid");
        }

        String tokenGerado = applicationUserService.signUpApplicationUser(
                new ApplicationUser(
                        userRequest.getFirstName(),
                        userRequest.getLastName(),
                        userRequest.getEmail(),
                        userRequest.getPassword(),
                        ApplicationUserRole.USER));

        //        TODO: Send Email with Confirmation Token
        String link = "http://localhost:8080/api/v1/full-registration/confirmation?token=" + tokenGerado;

        /* Note 1: Envio de Email utilizando AmigosCode interface EmailSender | EmailSenderImpl*/
        emailSender.send(userRequest.getEmail(), emailBody.buildEmail(userRequest.getFirstName(), link));

            /* Note 2: Envio de Email utilizando ObjetoJavaGmail.class*/
//            ArrayList<String> destinatarios = new ArrayList<>();
//            destinatarios.add(userRequest.getEmail());
//            objetoJavaGMail.enviarEmail(
//                    Constantes.REMETENTE.getValue(),
//                    Constantes.PASSWORD_REMETENTE.getValue(),
//                    "Paco Project",
//                    destinatarios,
//                    "Confirm your email",
//                    emailBody.buildEmail(userRequest.getFirstName(), link),
//                    true,
//                    new ArrayList<>());
        /* Note 3: Envio de email SIMPLES utilizando Documentacao do Spring Mail*/
//        emailService.sendSimpleMail(
//                new EmailDetails(
//                        userRequest.getEmail(),
//                        emailBody.buildEmail(userRequest.getFirstName(), link),
//                        "Confirm your email",
//                        null));
        /* Note 4: Envio de email Completo com attachment utilizando Documentacao do Spring Mail*/
//        emailService.sendMailWithAttachment(
//                new EmailDetails(
//                        userRequest.getEmail(),
//                        emailBody.buildEmail(userRequest.getFirstName(), link),
//                        "Confirm your email",
//                        "Y:/Download/relatorio.pdf"));

        return ResponseEntity.ok(link);
    }
}
