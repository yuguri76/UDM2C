package com.example.livealone.global.mail;

import com.example.livealone.global.exception.CustomException;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

  private final JavaMailSender mailSender;
  private final MessageSource messageSource;

  @Async
  public void sendEmail(String toEmail, String title, String content) {
    SimpleMailMessage message = createEmailForm(toEmail, title, content);

    try {
      mailSender.send(message);
    } catch (Exception e) {
      throw new CustomException(messageSource.getMessage(
          "fail.send.email",
          null,
          CustomException.DEFAULT_ERROR_MESSAGE,
          Locale.getDefault()
      ), HttpStatus.BAD_REQUEST);
    }
  }

  private SimpleMailMessage createEmailForm(String toEmail, String title, String content) {
    SimpleMailMessage message = new SimpleMailMessage();

    message.setTo(toEmail);
    message.setSubject(title);
    message.setText(content);

    return message;
  }

}
