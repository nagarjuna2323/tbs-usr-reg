package com.example.usr_reg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.usr_reg.model.Ticket;
import com.example.usr_reg.model.User;

import java.time.LocalDate;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    Optional<Ticket> findByUserAndBookingDate(User user, LocalDate bookingDate);
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.bookingDate = :bookingDate")
    long countTicketsByBookingDate(@Param("bookingDate") LocalDate bookingDate);
    
    boolean existsByUserAndBookingDate(User user, LocalDate bookingDate);
}
