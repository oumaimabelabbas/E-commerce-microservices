package com.oumaima.notification.kafka;

import com.oumaima.notification.email.EmailService;
import com.oumaima.notification.kafka.order.OrderConfirmation;
import com.oumaima.notification.kafka.payment.PaymentConfirmation;
import com.oumaima.notification.notification.Notification;
import com.oumaima.notification.notification.NotificationRepository;
import com.oumaima.notification.notification.NotificationType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    //listen to the payment topic to get the paymentconfirmation msg
    @KafkaListener(topics="payment-topic" )
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info(String.format("Consuming the message from payment-topic Topic:: %s",paymentConfirmation));
        // save msg in db
        notificationRepository.save(Notification.builder()
                        .notificationType(NotificationType.PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                .paymentConfirmation(paymentConfirmation)
                .build());
        //send email
        emailService.sendPaymentSuccessEmail(
                paymentConfirmation.customerEmail(),
                paymentConfirmation.customerFirstname()+ " "+
                paymentConfirmation.customerLastname(),
                paymentConfirmation.amount(),
                paymentConfirmation.orderReference()
        );


    }

    @KafkaListener(topics="order-topic" )
    public void consumeOrderSuccessNotification(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info(String.format("Consuming the message from order-topic Topic:: %s",orderConfirmation));
        // save msg in db
        notificationRepository.save(Notification.builder()
                .notificationType(NotificationType.ORDER_CONFIRMATION)
                .notificationDate(LocalDateTime.now())
                .orderConfirmation(orderConfirmation)
                .build());
        //send email
        emailService.sendOrderConfirmationEmail(
                orderConfirmation.customer().email(),
                orderConfirmation.customer().firstname()+ " "+ orderConfirmation.customer().lastname(),
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()
        );



    }
}
