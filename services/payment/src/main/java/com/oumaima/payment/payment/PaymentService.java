package com.oumaima.payment.payment;

import com.oumaima.payment.notification.NotificationProducer;
import com.oumaima.payment.notification.PaymentNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final NotificationProducer notificationProducer;
    public Integer createPayment(PaymentRequest request) {
        var payment =  paymentRepository.save(paymentMapper.toPayment(request));
        //send notif of payment
        notificationProducer.sendNotification(new PaymentNotificationRequest(
                request.orderReference(),
                request.amount(),
                request.paymentMethod(),
                request.customer().firstname(),
                request.customer().lastname(),
                request.customer().email()
        ));
        return payment.getId();

    }
}
