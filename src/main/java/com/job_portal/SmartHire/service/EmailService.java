package com.job_portal.SmartHire.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail,String name){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("Welcome to our Platform");
        simpleMailMessage.setText("Hello "+name+",\n\nThanks for registering with us!\n\nRegards,\nAuth Team");
        javaMailSender.send(simpleMailMessage);
    }

    public void sendResetOtpEmail(String toEmail,String otp){
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Password Reset OTP");
        mailMessage.setText("Your OTP for resetting your password is: "+otp+" .Use this OTP to proceed with resetting password");
        javaMailSender.send(mailMessage);
    }

    public void sendAuthenticationOtpRequest(String toEmail,String otp){
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Account Verification OTP");
        mailMessage.setText("Your OTP is: "+otp+" .Verify your account using this OTP");
        javaMailSender.send(mailMessage);
    }
}
