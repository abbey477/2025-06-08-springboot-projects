package com.javatechie.controller;

import com.javatechie.dto.PaymentRequest;
import com.javatechie.service.PaymentService;
import com.javatechie.service.impl.PaypalPaymentService;
import com.javatechie.service.impl.RazorpayPaymentService;
import com.javatechie.service.impl.StripePaymentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    public static final String PAYPAL = "paypal";
    public static final String RAZORPAY = "razorpay";
    public static final String STRIPE = "stripe";

    private final PaypalPaymentService paypalPaymentService;
    private final RazorpayPaymentService razorpayPaymentService;
    private final StripePaymentService stripePaymentService;

    public PaymentController(PaypalPaymentService paypalPaymentService,
                             RazorpayPaymentService razorpayPaymentService,
                             StripePaymentService stripePaymentService) {
        this.paypalPaymentService = paypalPaymentService;
        this.razorpayPaymentService = razorpayPaymentService;
        this.stripePaymentService = stripePaymentService;
    }

    @PostMapping("/pay")
    public String pay(@RequestBody PaymentRequest paymentRequest) {

        String amount = paymentRequest.getAmount();
        String paymentType = paymentRequest.getPaymentType();
        String sender = paymentRequest.getSender();
        String receiver = paymentRequest.getReceiver();

        return switch (paymentType.toLowerCase()) {
            case PAYPAL ->
                    paypalPaymentService.pay(amount, paymentType, sender, receiver);
            case RAZORPAY ->
                    razorpayPaymentService.pay(amount, paymentType, sender, receiver);
            case STRIPE ->
                    stripePaymentService.pay(amount, paymentType, sender, receiver);
            default ->
                    throw new IllegalArgumentException("Unsupported payment mode: " + paymentType);
        };
    }
}
