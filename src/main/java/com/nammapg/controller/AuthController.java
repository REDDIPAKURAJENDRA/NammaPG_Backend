package com.nammapg.controller;

import com.nammapg.dto.AuthRequest;
import com.nammapg.dto.AuthResponse;
import com.nammapg.dto.RegisterRequest;
import com.nammapg.dto.SendOtpRequest;
import com.nammapg.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        authService.sendRegistrationOtp(request.getEmail(), request.getName());
        return ResponseEntity.ok().body(Map.of("message", "OTP sent successfully"));
    }

    @PostMapping("/register-owner")
    public ResponseEntity<AuthResponse> registerOwner(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerOwner(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
