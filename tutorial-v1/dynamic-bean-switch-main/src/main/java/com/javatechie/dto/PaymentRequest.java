package com.javatechie.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String amount;
    private String paymentType;
    private String sender;
    private String receiver;
}
