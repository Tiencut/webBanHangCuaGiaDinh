package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Order;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.PaymentTransaction;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.OrderService;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
@Slf4j
public class CheckoutController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    @PostMapping("/initiate")
    public ResponseEntity<?> initiateCheckout(@RequestBody Map<String, Object> body) {
        try {
            Long orderId = Long.valueOf(String.valueOf(body.get("orderId")));
            String provider = String.valueOf(body.getOrDefault("provider", "VNPAY"));

            // fetch order to get amount
            var odto = orderService.getOrderById(orderId);
            BigDecimal amount = odto.getTotalAmount();

            // build redirect url (stub) — in real integration compute secure hash and provider endpoint
            String base = provider.equalsIgnoreCase("MOMO") ? System.getenv().getOrDefault("MOMO_ENDPOINT","https://sandbox.momo.vn/checkout") : System.getenv().getOrDefault("VNPAY_ENDPOINT","https://sandbox.vnpayment.vn/paymentv2/vpcpay.html");
            String params = "orderId=" + URLEncoder.encode(String.valueOf(orderId), StandardCharsets.UTF_8) + "&amount=" + URLEncoder.encode(amount.toString(), StandardCharsets.UTF_8);
            String redirectUrl = base + "?" + params;

            PaymentTransaction pt = paymentService.createPayment(orderId, provider.toUpperCase(), amount, redirectUrl);
            return ResponseEntity.ok(Map.of("redirectUrl", redirectUrl, "paymentId", pt.getId()));
        } catch (Exception e) {
            log.error("Error initiating checkout", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
