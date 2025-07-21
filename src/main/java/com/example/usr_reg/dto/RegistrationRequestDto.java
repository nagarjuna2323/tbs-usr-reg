package com.example.usr_reg.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class RegistrationRequestDto {
    @NotBlank
    private String fullName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 10, max = 20)
    private String phone;
}
