package com.example.usr_reg.dto;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class TicketBookingResponseDto {
    private String message;
    private boolean bookingSuccessful;
    private Long ticketId;
    private String userEmail;
    private LocalDate bookingDate;
    private LocalDateTime bookedAt;
}
