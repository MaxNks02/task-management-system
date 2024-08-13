package com.example.systemtaskmanagement.config.security.service;

import com.example.systemtaskmanagement.config.security.model.RefreshToken;
import com.example.systemtaskmanagement.config.security.repo.RefreshTokenRepo;
import com.example.systemtaskmanagement.domain.dao.model.User;
import com.example.systemtaskmanagement.domain.dao.repo.UserRepo;
import com.example.systemtaskmanagement.exeption.CustomNotFoundException;
import com.example.systemtaskmanagement.exeption.UnauthorizedException;
import com.example.systemtaskmanagement.exeption.handling.ApiMessages;
import com.example.systemtaskmanagement.payload.request.RefreshTokenRequest;
import com.example.systemtaskmanagement.payload.response.RefreshTokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final UserRepo userRepository;
    private final RefreshTokenRepo refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    @Value("${application.security.jwt.refresh-token.cookie-name}")
    private String refreshTokenName;

    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        RefreshToken refreshToken = RefreshToken.builder()
                .revoked(false)
                .user(user)
                .token(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()))
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }


    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token == null) {

            throw new UnauthorizedException(String.format(ApiMessages.UNAUTHORIZED + " %s", "Token is null"));
        }
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new UnauthorizedException(String.format(ApiMessages.UNAUTHORIZED + " %s", "Refresh token was expired. Please make a new authentication request"));
        }
        return token;
    }


    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }


    public RefreshTokenResponse generateNewToken(RefreshTokenRequest request) {
        User user = refreshTokenRepository.findByToken(request.getRefreshToken())
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)

                .orElseThrow(() -> new CustomNotFoundException(String.format(ApiMessages.UNAUTHORIZED + " %s", "Refresh token does not exist")));


        String token = jwtService.generateToken(user);
        return RefreshTokenResponse.builder()
                .accessToken(token)
                .refreshToken(request.getRefreshToken())
                .tokenType("BEARER")
                .build();
    }


    public ResponseCookie generateRefreshTokenCookie(String token) {
        return ResponseCookie.from(refreshTokenName, token)
                .path("/")
                .maxAge(refreshExpiration / 1000) // 15 days in seconds
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }


    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, refreshTokenName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return "";
        }
    }


    public void deleteByToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }


    public ResponseCookie getCleanRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenName, "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
    }
}