package com.nammapg.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String to, String name, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // true indicates multipart message for HTML
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("Your NammaPG OTP Verification Code");
            
            String htmlContent = String.format("""
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden;">
                    <div style="background-color: #4F46E5; color: white; padding: 20px; text-align: center;">
                        <h1 style="margin: 0; font-size: 24px;">NammaPG</h1>
                    </div>
                    <div style="padding: 30px; background-color: #ffffff;">
                        <h2 style="color: #333333; margin-top: 0;">Verify Your Email Address</h2>
                        <p style="color: #555555; font-size: 16px; line-height: 1.5;">Hello <strong>%s</strong>,</p>
                        <p style="color: #555555; font-size: 16px; line-height: 1.5;">Thank you for partnering with NammaPG! Please use the following One-Time Password (OTP) to complete your registration request:</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <span style="display: inline-block; padding: 15px 30px; background-color: #f3f4f6; color: #111827; font-size: 32px; font-weight: bold; letter-spacing: 5px; border-radius: 8px; border: 1px dashed #4F46E5;">%s</span>
                        </div>
                        <p style="color: #555555; font-size: 14px; line-height: 1.5;">This code is valid for <strong>5 minutes</strong>. If you did not request this registration, please safely ignore this email.</p>
                        <hr style="border: none; border-top: 1px solid #eeeeee; margin: 30px 0;" />
                        <p style="color: #888888; font-size: 12px; text-align: center; margin: 0;">&copy; 2024 NammaPG. All rights reserved.</p>
                    </div>
                </div>
                """, (name != null ? name : "User"), otp);
            
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email. Please check configuration.");
        }
    }
}
