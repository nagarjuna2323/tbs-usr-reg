package com.example.usr_reg.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class TicketBookingRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotNull
    @FutureOrPresent
    private LocalDate bookingDate;
}
