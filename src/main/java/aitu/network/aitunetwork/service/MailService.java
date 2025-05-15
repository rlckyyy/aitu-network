package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.entity.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;

    public void sendConfirmationMessage(User user) {
        String confirmationUrl = "https://aitunet.kz/api/v1/auth/confirm?token=" + user.getVerificationToken();

        MimeMessage message = mailSender.createMimeMessage();

        try {
            Map<String, Object> model = new HashMap<>();
            model.put("name", user.getUsername());
            model.put("confirmationUrl", confirmationUrl);

            Template template = freemarkerConfig.getTemplate("confirmation_email.ftl");
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            String html = writer.toString();

            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
            helper.setFrom("noreply@aitu.kz");
            helper.setTo(user.getEmail());
            helper.setSubject("Confirm your email");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException | IOException | TemplateException e) {
            throw new RuntimeException("Failed to send confirmation email", e);
        }
    }
}
