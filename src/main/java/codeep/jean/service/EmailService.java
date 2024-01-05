package codeep.jean.service;

import codeep.jean.service.dto.EmailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private static final String FROM_ADDRESS = "sunwoo1137@gmail.com";
    @Async
    public void mailSend(EmailDTO emailDTO){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_ADDRESS);
        message.setTo(emailDTO.getAddress());
        message.setSubject(emailDTO.getTitle());
        message.setText(emailDTO.getMessage());
        javaMailSender.send(message);
    }
}