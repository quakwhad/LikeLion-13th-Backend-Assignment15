package com.likelion.likelionmail.mail.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "your_email@gmail.com"; // 발신자

    public void sendTestMail(String to) {
        String subject = "[Likelion JWT] 이메일 전송 테스트";
        String body = """
            <div style="font-family: Arial, sans-serif; padding: 20px;">
                <h2>안녕하세요 🦁</h2>
                <p>이 메일은 <strong>Likelion JWT 프로젝트</strong>의 SMTP 테스트 메일입니다.</p>
                <p style="color: gray;">메일이 정상적으로 도착했다면 SMTP 설정이 성공한 것입니다!</p>
                <hr>
                <p style="font-size: 12px; color: #888;">© 2025 Likelion Project</p>
            </div>
            """;

        try {
            sendHtmlMail(to, subject, body);
            System.out.println("메일 전송 성공! 대상: " + to);
        } catch (MessagingException e) {
            e.printStackTrace(); // 로그 출력
            throw new RuntimeException("메일 전송 실패ㅜ: " + e.getMessage());
        }
    }

    public void sendDailyMail(String to) {
        String subject = "[Likelion JWT] 매분마다 실행";
        String body = """
            <div style="font-family: Arial, sans-serif; padding: 20px;">
                <h2>안녕하세요 🦁</h2>
                <p>이 메일은 <strong>Likelion JWT 프로젝트</strong>의 sendDailyMail 테스트 메일입니다.</p>
                <p style="color: gray;">메일이 정상적으로 도착했다면 sendDailyMail 설정이 성공한 것입니다!</p>
                <hr>
                <p style="font-size: 12px; color: #888;">© 2025 Likelion Project</p>
            </div>
            """;

        try {
            sendHtmlMail(to, subject, body);
            System.out.println("메일 전송 성공! 대상: " + to);
        } catch (MessagingException e) {
            e.printStackTrace(); // 로그 출력
            throw new RuntimeException("메일 전송 실패ㅜ: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void scheduleDailyMail() {
        String fixedTarget = "quakwhad@gmail.com";

        log.info("스케줄러 실행: 데일리 메일 발송 시작");
        sendDailyMail(fixedTarget);
    }

    private void sendHtmlMail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(FROM_ADDRESS);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        mailSender.send(message);
    }
}
