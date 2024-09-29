package com.d106.arti.login.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@Service
public class MailService {

    private static String number;

    private final JavaMailSender javaMailSender;
    private final String senderEmail;

    @Autowired
    public MailService(JavaMailSender javaMailSender, @Value("${mail.senderEmail}") String senderEmail) {
        this.javaMailSender = javaMailSender;
        this.senderEmail = senderEmail;
    }

    public static void createNumber() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 6; i++) { // 6자리 숫자 생성
            key.append(random.nextInt(10)); // 0-9 숫자 랜덤 생성
        }
        number = key.toString();
    }

    public MimeMessage createMessage(String email) {
        createNumber();
        log.info("Number : {}", number);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

            // InternetAddress를 사용하여 이메일과 발신자 이름 설정
            messageHelper.setFrom(new InternetAddress(senderEmail, "Arti"));
            messageHelper.setTo(email);
            messageHelper.setSubject("Arti 회원가입 인증용 메일입니다.");

            String body = "<html><body style='background-color: #000000 !important; margin: 0 auto; max-width: 600px; word-break: break-all; padding-top: 50px; color: #ffffff;'>";
            body += "<h1 style='padding-top: 50px; font-size: 30px;'>이메일 주소 인증</h1>";
            body += "<p style='padding-top: 20px; font-size: 18px; opacity: 0.6; line-height: 30px; font-weight: 400;'>안녕하세요? Arti 관리자 입니다.<br />";
            body += "Arti 서비스 사용을 위해 회원가입시 고객님께서 입력하신 이메일 주소의 인증이 필요합니다.<br />";
            body += "하단의 인증 번호로 이메일 인증을 완료하시면, 정상적으로 Arti 서비스를 이용하실 수 있습니다.<br />";
            body += "감사합니다.</p>";
            body += "<div class='code-box' style='margin-top: 50px; padding-top: 20px; color: #000000; padding-bottom: 20px; font-size: 25px; text-align: center; background-color: #f4f4f4; border-radius: 10px;'>" + number + "</div>";
            body += "</body></html>";
            messageHelper.setText(body, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return mimeMessage;
    }

    public String sendMail(String email) {
        MimeMessage mimeMessage = createMessage(email);
        log.info("[Mail 전송 시작]");
        javaMailSender.send(mimeMessage);
        log.info("[Mail 전송 완료]");
        return number;
    }
}
