package codeep.jean.controller;

import codeep.jean.service.EmailService;
import codeep.jean.service.dto.EmailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    @PostMapping("/mail")
    public void execMail(@ModelAttribute  EmailDTO emailDTO) {
        emailService.mailSend(emailDTO);

    }
}
