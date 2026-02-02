package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.PaymentTransaction;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentTransactionRepository paymentTransactionRepository;

    @Transactional
    public PaymentTransaction createPayment(Long orderId, String provider, BigDecimal amount, String redirectUrl) {
        PaymentTransaction pt = new PaymentTransaction();
        pt.setOrderId(orderId);
        pt.setProvider(provider);
        pt.setAmount(amount);
        pt.setRedirectUrl(redirectUrl);
        pt.setStatus(PaymentTransaction.Status.PENDING);
        return paymentTransactionRepository.save(pt);
    }

    public Optional<PaymentTransaction> findByProviderTxnId(String providerTxnId) {
        return paymentTransactionRepository.findByProviderTxnId(providerTxnId);
    }

    @Transactional
    public PaymentTransaction markSuccess(PaymentTransaction pt, String providerTxnId) {
        pt.setStatus(PaymentTransaction.Status.SUCCESS);
        pt.setProviderTxnId(providerTxnId);
        pt.setWebhookProcessed(true);
        return paymentTransactionRepository.save(pt);
    }

    @Transactional
    public PaymentTransaction markFailed(PaymentTransaction pt, String providerTxnId) {
        pt.setStatus(PaymentTransaction.Status.FAILED);
        pt.setProviderTxnId(providerTxnId);
        pt.setWebhookProcessed(true);
        return paymentTransactionRepository.save(pt);
    }
}
