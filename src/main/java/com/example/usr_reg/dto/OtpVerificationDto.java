package com.example.usr_reg.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class OtpVerificationDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String otp;
}
