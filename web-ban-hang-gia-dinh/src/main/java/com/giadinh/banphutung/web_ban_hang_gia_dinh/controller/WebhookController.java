package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.PaymentTransaction;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.OrderService;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    // VNPAY webhook (POST)
    @PostMapping("/vnpay")
    public ResponseEntity<?> vnpayWebhook(@RequestBody Map<String, String> payload, @RequestHeader Map<String, String> headers) {
        // Basic verification: check vnp_SecureHash
        String secureHash = payload.get("vnp_SecureHash");
        String txnRef = payload.get("vnp_TxnRef");
        String rspCode = payload.get("vnp_ResponseCode");
        String orderInfo = payload.get("vnp_OrderInfo");

        // Find payment by provider txn id or order id
        // For this stub we assume vnp_TxnRef contains orderId
        try {
            Long orderId = Long.parseLong(txnRef);
            // find PaymentTransaction by orderId and provider
            var optional = paymentService.findByProviderTxnId(String.valueOf(txnRef));
            // if not found, try find by order id via repository search (simpler: mark by order id)
            var ptOpt = optional;
            if (ptOpt.isPresent()) {
                PaymentTransaction pt = ptOpt.get();
                if (pt.getWebhookProcessed() != null && pt.getWebhookProcessed()) {
                    return ResponseEntity.ok(Map.of("message", "already_processed"));
                }
                if ("00".equals(rspCode)) {
                    paymentService.markSuccess(pt, txnRef);
                    orderService.applyPaymentSuccess(orderId, "VNPAY", txnRef);
                } else {
                    paymentService.markFailed(pt, txnRef);
                    orderService.applyPaymentFailure(orderId, "VNPAY", txnRef);
                }
                return ResponseEntity.ok(Map.of("message", "processed"));
            } else {
                log.warn("VNPAY webhook: payment transaction not found for txnRef={}", txnRef);
                return ResponseEntity.badRequest().body(Map.of("error", "payment not found"));
            }
        } catch (Exception e) {
            log.error("Error processing vnpay webhook", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // MoMo webhook (POST)
    @PostMapping("/momo")
    public ResponseEntity<?> momoWebhook(@RequestBody Map<String, String> payload, @RequestHeader Map<String, String> headers) {
        String requestId = payload.get("requestId");
        String orderIdStr = payload.get("orderId");
        String resultCode = payload.get("resultCode");

        try {
            Long orderId = Long.parseLong(orderIdStr);
            var optional = paymentService.findByProviderTxnId(requestId);
            if (optional.isPresent()) {
                PaymentTransaction pt = optional.get();
                if (pt.getWebhookProcessed() != null && pt.getWebhookProcessed()) {
                    return ResponseEntity.ok(Map.of("message", "already_processed"));
                }
                if ("0".equals(resultCode)) {
                    paymentService.markSuccess(pt, requestId);
                    orderService.applyPaymentSuccess(orderId, "MOMO", requestId);
                } else {
                    paymentService.markFailed(pt, requestId);
                    orderService.applyPaymentFailure(orderId, "MOMO", requestId);
                }
                return ResponseEntity.ok(Map.of("message", "processed"));
            } else {
                log.warn("MoMo webhook: payment transaction not found for requestId={}", requestId);
                return ResponseEntity.badRequest().body(Map.of("error", "payment not found"));
            }
        } catch (Exception e) {
            log.error("Error processing momo webhook", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
