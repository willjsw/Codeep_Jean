package codeep.jean;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    @Bean
    public JavaMailSender javaMailSender(
            @Value("${spring.mail.host}")String host,
            @Value("${spring.mail.port}")Integer port,
            @Value("${spring.mail.username}")String username,
            @Value("${spring.mail.password}")String password,
            @Value("${spring.mail.properties.mail.smtp.auth}")String auth,
            @Value("${spring.mail.properties.mail.smtp.starttls.enable}")String enable) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
