package com.example.bkb.service;

import com.example.bkb.domain.entity.User;
import com.example.bkb.dto.AuthResponse;
import com.example.bkb.dto.LoginRequest;
import com.example.bkb.dto.RegisterRequest;
import com.example.bkb.exception.ApiException;
import com.example.bkb.repository.UserRepository;
import com.example.bkb.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.findByEmail(req.email()).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "Email already registered");
        }
        var user = User.builder()
                .email(req.email())
                .passwordHash(passwordEncoder.encode(req.password()))
                .fullName(req.fullName())
                .role(req.role())
                .build();
        userRepository.save(user);
        return new AuthResponse(jwtUtils.generateToken(user));
    }

    public AuthResponse login(LoginRequest req) {
        var user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return new AuthResponse(jwtUtils.generateToken(user));
    }
}
