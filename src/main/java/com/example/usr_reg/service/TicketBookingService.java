package com.example.usr_reg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.usr_reg.repository.TicketRepository;
import com.example.usr_reg.repository.UserRepository;
import com.example.usr_reg.model.Ticket;
import com.example.usr_reg.model.User;
import com.example.usr_reg.dto.TicketBookingRequestDto;
import com.example.usr_reg.dto.TicketBookingResponseDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class TicketBookingService {
    
    private static final int MAX_TICKETS_PER_DAY = 5;
    private static final String OTP_STATUS_VERIFIED = "Yes";
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private UserRepository userRepository;

    public TicketBookingResponseDto bookTicket(TicketBookingRequestDto dto, String bookingIp) {
        if (dto == null) {
            throw new IllegalArgumentException("Booking data cannot be null");
        }
        if (bookingIp == null || bookingIp.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking IP cannot be null or empty");
        }

        Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + dto.getEmail());
        }

        User user = userOpt.get();

        if (!OTP_STATUS_VERIFIED.equalsIgnoreCase(user.getOtpStatus())) {
            throw new IllegalStateException("User must complete OTP verification before booking tickets");
        }

        if (!user.getRegistrationIp().equals(bookingIp)) {
            throw new IllegalStateException("Booking IP must match the IP used during registration");
        }

        if (ticketRepository.existsByUserAndBookingDate(user, dto.getBookingDate())) {
            throw new IllegalStateException("User can only book one ticket per day");
        }

        long existingTicketsForDate = ticketRepository.countTicketsByBookingDate(dto.getBookingDate());
        if (existingTicketsForDate >= MAX_TICKETS_PER_DAY) {
            throw new IllegalStateException("Maximum " + MAX_TICKETS_PER_DAY + " tickets per day limit reached");
        }

        Ticket ticket = Ticket.builder()
                .user(user)
                .bookingDate(dto.getBookingDate())
                .bookingIp(bookingIp)
                .bookedAt(LocalDateTime.now())
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        return TicketBookingResponseDto.builder()
                .message("Ticket booked successfully")
                .bookingSuccessful(true)
                .ticketId(savedTicket.getId())
                .userEmail(user.getEmail())
                .bookingDate(savedTicket.getBookingDate())
                .bookedAt(savedTicket.getBookedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public long getAvailableTicketsForDate(java.time.LocalDate date) {
        long bookedTickets = ticketRepository.countTicketsByBookingDate(date);
        return Math.max(0, MAX_TICKETS_PER_DAY - bookedTickets);
    }
}
