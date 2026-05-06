package com.grihom.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendWelcomeEmail(String toEmail, String name) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to GriHom 2.0!");
            message.setText(
                "Dear " + name + ",\n\n" +
                "Welcome to GriHom 2.0 — India's home improvement advisory platform.\n\n" +
                "You can now:\n" +
                "  • Generate your property Valor Score\n" +
                "  • Get personalized improvement recommendations\n" +
                "  • Save and track your reports\n" +
                "  • Plan your home upgrades\n\n" +
                "Get started: http://localhost:3000\n\n" +
                "Warm regards,\n" +
                "Team GriHom"
            );
            mailSender.send(message);
            log.info("Welcome email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}", toEmail, e.getMessage());
        }
    }

    @Async
    public void sendReportEmail(String toEmail, String name, String reportTitle, int score) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("GriHom Report Saved: " + reportTitle);
            message.setText(
                "Dear " + name + ",\n\n" +
                "Your property report has been saved successfully.\n\n" +
                "Report: " + reportTitle + "\n" +
                "Valor Score: " + score + " / 100\n\n" +
                "View your dashboard: http://localhost:3000/dashboard\n\n" +
                "Warm regards,\n" +
                "Team GriHom"
            );
            mailSender.send(message);
            log.info("Report email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send report email to {}: {}", toEmail, e.getMessage());
        }
    }

    @Async
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("GriHom — Password Reset Request");
            message.setText(
                "Hello,\n\n" +
                "We received a password reset request for your GriHom account.\n\n" +
                "Reset link: http://localhost:3000/reset-password?token=" + resetToken + "\n\n" +
                "This link expires in 1 hour.\n\n" +
                "If you did not request a reset, ignore this email.\n\n" +
                "Team GriHom"
            );
            mailSender.send(message);
            log.info("Password reset email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage());
        }
    }
}
