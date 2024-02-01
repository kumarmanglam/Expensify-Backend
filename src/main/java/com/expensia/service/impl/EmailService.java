package com.expensia.service.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Set Gmail SMTP server host
        mailSender.setHost("smtp.gmail.com");
        // Set the port for Gmail SMTP (TLS) - 587
        mailSender.setPort(587);
        // Set your Gmail username and password
        mailSender.setUsername("kumarbackups01@gmail.com");
        mailSender.setPassword("jtfnouqfjaklbrgo");
        Properties properties = mailSender.getJavaMailProperties();

        // Enable SMTP authentication
        properties.put("mail.smtp.auth", "true");
        // Enable STARTTLS for secure communication
        properties.put("mail.smtp.starttls.enable", "true");
        // Set the protocol to use SMTP
        properties.put("mail.transport.protocol", "smtp");
        // Enable debug mode for troubleshooting (optional)
        properties.put("mail.debug", "true");
        return mailSender;
    }
    public void sendPasswordResetEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender().send(message);
    }
}
