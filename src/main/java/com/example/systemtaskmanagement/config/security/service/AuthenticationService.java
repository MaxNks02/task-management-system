package com.example.systemtaskmanagement.config.security.service;

import com.example.systemtaskmanagement.domain.dao.model.User;
import com.example.systemtaskmanagement.domain.dao.model.enums.ERole;
import com.example.systemtaskmanagement.domain.dao.repo.UserRepo;
import com.example.systemtaskmanagement.payload.request.AuthenticationRequest;
import com.example.systemtaskmanagement.payload.request.RegisterRequest;
import com.example.systemtaskmanagement.payload.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service @Transactional
@RequiredArgsConstructor
public class AuthenticationService  {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepo userRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(ERole.USER)
                .build();
        user = userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());



        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .email(user.getEmail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .tokenType("BEARER")
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return AuthenticationResponse.builder()
                .accessToken(jwt)

                .email(user.getEmail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .tokenType("BEARER")
                .build();
    }
}