package com.example.usr_reg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import com.example.usr_reg.service.TicketBookingService;
import com.example.usr_reg.dto.TicketBookingRequestDto;
import com.example.usr_reg.dto.TicketBookingResponseDto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketBookingController {
    
    @Autowired
    private TicketBookingService ticketBookingService;

    @PostMapping("/book")
    public ResponseEntity<TicketBookingResponseDto> bookTicket(
            @RequestBody @Valid TicketBookingRequestDto dto,
            HttpServletRequest request
    ) {
        try {
            String clientIp = request.getRemoteAddr();
            TicketBookingResponseDto response = ticketBookingService.bookTicket(dto, clientIp);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                TicketBookingResponseDto.builder()
                    .message(e.getMessage())
                    .bookingSuccessful(false)
                    .build()
            );
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(
                TicketBookingResponseDto.builder()
                    .message(e.getMessage())
                    .bookingSuccessful(false)
                    .build()
            );
        }
    }

    @GetMapping("/availability")
    public ResponseEntity<Map<String, Object>> getTicketAvailability(
            @RequestParam("date") String dateStr
    ) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            long availableTickets = ticketBookingService.getAvailableTicketsForDate(date);
            
            Map<String, Object> response = new HashMap<>();
            response.put("date", date);
            response.put("availableTickets", availableTickets);
            response.put("maxTicketsPerDay", 5);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid date format. Use YYYY-MM-DD");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
