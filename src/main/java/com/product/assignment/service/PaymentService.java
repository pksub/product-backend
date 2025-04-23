package com.product.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.product.assignment.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private  RestTemplate restTemplate;

    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${payment.api.url}")
    private String paymentApiUrl;

    public String processPayment(Long orderId, Long amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("orderId", orderId.toString());
        requestBody.put("amount", amount);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        Map<String, Object> response = restTemplate.postForObject(
                paymentApiUrl + "/api/v1/payment",
                request,
                Map.class
        );

        if (response != null && "SUCCESS".equals(response.get("status"))) {
            return (String) response.get("transactionId");
        } else {
            throw new BusinessException("결제 처리에 실패했습니다.");
        }
    }
} 