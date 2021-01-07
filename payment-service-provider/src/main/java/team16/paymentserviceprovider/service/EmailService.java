package team16.paymentserviceprovider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private Environment env;

    @Async
    public void sendEmail(String email, String subject, String text) throws MailException, InterruptedException, MessagingException, MessagingException {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setText(text);
        mail.setTo(email);
        mail.setSubject(subject);
        mail.setFrom(env.getProperty("spring.mail.username"));
        javaMailSender.send(mail);
    }
}
