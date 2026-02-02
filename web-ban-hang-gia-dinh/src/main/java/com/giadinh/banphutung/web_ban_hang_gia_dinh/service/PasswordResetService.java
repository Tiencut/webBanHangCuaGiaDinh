package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.PasswordResetToken;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.PasswordResetTokenRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public PasswordResetToken createTokenForEmail(String email, String frontendResetUrl) {
        Optional<User> userOpt = userRepository.findByEmailAndIsActiveTrue(email);
        if (userOpt.isEmpty()) return null;

        User user = userOpt.get();
        String token = UUID.randomUUID().toString();

        PasswordResetToken prt = new PasswordResetToken();
        prt.setToken(token);
        prt.setUser(user);
        prt.setExpiryDate(LocalDateTime.now().plusHours(4));
        prt.setUsed(false);
        tokenRepository.save(prt);

        // send email
        String link = frontendResetUrl + "?token=" + token;
        String subject = "Yêu cầu đặt lại mật khẩu";
        String text = "Vui lòng mở liên kết sau để đặt lại mật khẩu (còn hiệu lực 4 giờ): " + link;
        emailService.sendSimpleMessage(user.getEmail(), subject, text);

        return prt;
    }

    public Optional<User> validateToken(String token) {
        Optional<PasswordResetToken> prt = tokenRepository.findByToken(token);
        if (prt.isEmpty()) return Optional.empty();
        PasswordResetToken t = prt.get();
        if (t.getUsed() || t.getExpiryDate().isBefore(LocalDateTime.now())) return Optional.empty();
        return Optional.of(t.getUser());
    }

    public boolean consumeToken(String token) {
        Optional<PasswordResetToken> prt = tokenRepository.findByToken(token);
        if (prt.isEmpty()) return false;
        PasswordResetToken t = prt.get();
        if (t.getUsed() || t.getExpiryDate().isBefore(LocalDateTime.now())) return false;
        t.setUsed(true);
        tokenRepository.save(t);
        return true;
    }
}
