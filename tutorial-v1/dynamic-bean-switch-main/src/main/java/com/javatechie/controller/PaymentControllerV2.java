package com.javatechie.controller;

import com.javatechie.dto.PaymentRequest;
import com.javatechie.service.PaymentService;
import com.javatechie.service.impl.PaypalPaymentService;
import com.javatechie.service.impl.RazorpayPaymentService;
import com.javatechie.service.impl.StripePaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payment/v2")
public class PaymentControllerV2 {


    private final Map<String, PaymentService> paymentServicesMap;

    public PaymentControllerV2(Map<String, PaymentService> paymentServicesMap) {
        this.paymentServicesMap = paymentServicesMap;
    }
    // key                      value
    // paypalPaymentService- PaypalPaymentService
    // paypal - PaypalPaymentService
    // razorpayPaymentService- RazorpayPaymentService
    // razorpay - RazorpayPaymentService
    // stripePaymentService- StripePaymentService
    // stripe - StripePaymentService

    @PostMapping("/pay/v2")
    public String pay(@RequestBody PaymentRequest paymentRequest) {

        String paymentType = paymentRequest.getPaymentType().toLowerCase();

        PaymentService service = paymentServicesMap.get(paymentType);

        if (service == null) {
            throw new IllegalArgumentException("Unsupported payment mode: " + paymentType);
        }
        return service.pay(
                paymentRequest.getAmount(),
                paymentType,
                paymentRequest.getSender(),
                paymentRequest.getReceiver()
        );

    }
}
