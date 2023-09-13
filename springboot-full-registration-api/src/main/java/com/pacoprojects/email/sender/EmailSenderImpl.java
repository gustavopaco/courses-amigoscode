package com.pacoprojects.email.sender;

import com.pacoprojects.model.Constantes;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@AllArgsConstructor
public class EmailSenderImpl implements EmailSender {


    private final static Logger LOGGER = LoggerFactory.getLogger(EmailSenderImpl.class);

    @Override
    @Async
    public void send(String to, String emailBody) {

        try {
            JavaMailSenderImpl emailSender = javaMailSenderBean();
            emailSender.setHost("smtp.gmail.com");
            emailSender.setUsername(Constantes.REMETENTE.getValue());
            emailSender.setPassword(Constantes.PASSWORD_REMETENTE.getValue());
            emailSender.setPort(465);
            Properties properties = new Properties();
            properties.put("mail.smtp.starttls", "true"); // Autenticacao
            properties.put("mail.smtp.auth", true);
            properties.put("mail.smtp.socketFactory.port", 465);
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            emailSender.setJavaMailProperties(properties);

            MimeMessage mimeMessage = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            /* Corpo do Email*/
            helper.setText(emailBody, true);

            /* Destinatario*/
            helper.setTo(to);

            /* Assunto do Email */
            helper.setSubject("Confirm your email");

            helper.setFrom("gustavopacotest@gmail.com");

            emailSender.send(mimeMessage);

        } catch (MessagingException exception) {
            LOGGER.error("Failed to send email", exception);
            throw new IllegalStateException("Failed to send email");
        }
    }


    @Autowired
    public JavaMailSenderImpl javaMailSenderBean() {
        return new JavaMailSenderImpl();
    }

}
