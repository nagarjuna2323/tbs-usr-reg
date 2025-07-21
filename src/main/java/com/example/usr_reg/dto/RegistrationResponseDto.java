package com.example.usr_reg.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class RegistrationResponseDto {
    private String message;
    private boolean otpVerified;
}
