package com.oumaima.notification.email;

import com.netflix.discovery.converters.Auto;
import com.oumaima.notification.kafka.order.Product;
import com.oumaima.notification.kafka.payment.PaymentConfirmation;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendPaymentSuccessEmail(String destinationEmail, String customerName, BigDecimal amount,String orderReference) throws MessagingException {
        MimeMessage  mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
                );
        final String templateName = EmailTemplate.PAYMENT_CONFIRMATION.getTemplate();
        Map<String,Object> variables = new HashMap<>();
        variables.put("customerName",customerName);
        variables.put("amount",amount);
        variables.put("orderReference",orderReference);

        Context context = new Context();
        context.setVariables(variables);
        //set the from who and subject of email
        messageHelper.setFrom("beloumaima56@gmail.com");
        messageHelper.setSubject(EmailTemplate.PAYMENT_CONFIRMATION.getSubject());
        try{
            String htmlTemplate = templateEngine.process(templateName,context);
            //context et templateName
            messageHelper.setText(htmlTemplate,true);
            //to who
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info(String.format("INFO - Email successfully send to the %s with template %s",destinationEmail,templateName));
        }catch (MessagingException  e){
            log.warn("WARNING Cannot send the email");
        }

    }
    @Async
    public void sendOrderConfirmationEmail(String destinationEmail, String customerName, BigDecimal amount, String orderReference, List<Product> products) throws MessagingException {
        MimeMessage  mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );
        final String templateName = EmailTemplate.ORDER_CONFIRMATION.getTemplate();
        Map<String,Object> variables = new HashMap<>();
        variables.put("customerName",customerName);
        variables.put("amount",amount);
        variables.put("orderReference",orderReference);
        variables.put("products",products);

        Context context = new Context();
        context.setVariables(variables);
        //set the from who and subject of email
        messageHelper.setFrom("beloumaima56@gmail.com");
        messageHelper.setSubject(EmailTemplate.ORDER_CONFIRMATION.getSubject());
        try{
            String htmlTemplate = templateEngine.process(templateName,context);
            //context et templateName
            messageHelper.setText(htmlTemplate,true);
            //to who
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info(String.format("INFO - Email successfully send to the %s with template %s",destinationEmail,templateName));
        }catch (MessagingException  e){
            log.warn("WARNING Cannot send the email");
        }

    }

}
