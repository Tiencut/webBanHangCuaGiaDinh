package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.RefreshToken;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // default refresh token validity (30 days)
    private static final long REFRESH_TOKEN_DAYS = Long.parseLong(System.getenv().getOrDefault("REFRESH_TOKEN_DAYS", "30"));

    public RefreshToken createTokenForUser(User user) {
        RefreshToken rt = new RefreshToken();
        rt.setToken(UUID.randomUUID().toString());
        rt.setUserId(user.getId());
        rt.setCreatedAt(Instant.now());
        rt.setExpiresAt(Instant.now().plus(REFRESH_TOKEN_DAYS, ChronoUnit.DAYS));
        rt.setRevoked(false);
        return refreshTokenRepository.save(rt);
    }

    public Optional<User> validateAndGetUser(String token, UserService userService) {
        Optional<RefreshToken> opt = refreshTokenRepository.findByToken(token);
        if (opt.isEmpty()) return Optional.empty();
        RefreshToken rt = opt.get();
        if (rt.isRevoked() || rt.getExpiresAt().isBefore(Instant.now())) return Optional.empty();
        return userService.getUserEntityById(rt.getUserId());
    }

    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }

    public void deleteToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
