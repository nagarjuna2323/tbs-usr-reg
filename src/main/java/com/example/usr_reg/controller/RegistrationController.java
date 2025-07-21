package com.example.usr_reg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.example.usr_reg.service.RegistrationService;
import com.example.usr_reg.dto.RegistrationRequestDto;
import com.example.usr_reg.dto.OtpVerificationDto;
import com.example.usr_reg.dto.RegistrationResponseDto;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @PostMapping
    public RegistrationResponseDto register(
            @RequestBody @Valid RegistrationRequestDto dto,
            HttpServletRequest request
    ) {
        String clientIp = request.getRemoteAddr();
        registrationService.register(dto, clientIp);
        return RegistrationResponseDto.builder()
                .message("Registration started. OTP sent to user.")
                .otpVerified(false)
                .build();
    }

    @PostMapping("/verify-otp")
    public RegistrationResponseDto verifyOtp(
            @RequestBody @Valid OtpVerificationDto dto
    ) {
        boolean verified = registrationService.verifyOtp(dto);
        return RegistrationResponseDto.builder()
                .message(verified ? "OTP verified. Registration complete." : "Invalid OTP.")
                .otpVerified(verified)
                .build();
    }
}
