package com.http200ok.finbuddy.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    // private static final Dotenv dotenv = Dotenv.load();
    // private static final String senderEmail = dotenv.get("SMTP_EMAIL");
    @Value("${smtp.email}")
    private static String senderEmail;
    private static final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Random random = new Random();

    // 인증번호 생성 및 저장
    private String generateCode() {
        return String.valueOf(100000 + random.nextInt(900000));
    }

    // 이메일 생성
    private MimeMessage createMail(String mail, String code) {

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(senderEmail);
            helper.setTo(mail);
            helper.setSubject("이메일 인증");
            String body = "<h2>FinBuddy</h2>" +
                    "<h3>요청하신 인증 번호입니다.</h3>" +
                    "<h1>" + code + "</h1>" +
                    "<h3>감사합니다.</h3>";
            helper.setText(body, true);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송 실패", e);
        }

        return message;
    }

    @Override
    public void sendMail(String mail) {
        String code = generateCode();
        verificationCodes.put(mail, code);
        MimeMessage message = createMail(mail, code);
        javaMailSender.send(message);
    }

    @Override
    public boolean verifyCode(String mail, String userCode) {
        return verificationCodes.getOrDefault(mail, "").equals(userCode);
    }
}
