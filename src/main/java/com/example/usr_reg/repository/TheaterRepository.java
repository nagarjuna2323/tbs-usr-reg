package com.example.usr_reg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.usr_reg.model.Theater;

import java.util.List;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    
    // Find theaters by name and location (case-insensitive) - for duplicate check
    List<Theater> findByNameAndLocationIgnoreCase(String name, String location);
    
    // Find theaters by location
    List<Theater> findByLocationContainingIgnoreCase(String location);
    
    // Find theaters by movie title
    List<Theater> findByMovieTitleContainingIgnoreCase(String movieTitle);
    
    // Find theaters with available seats
    List<Theater> findBySeatsAvailableGreaterThan(Integer seats);
    
    // Find theaters by show time
    List<Theater> findByShowTimeContainingIgnoreCase(String showTime);
    
    // Custom query to get theaters with availability status
    @Query("SELECT t FROM Theater t WHERE t.seatsAvailable > 0 ORDER BY t.seatsAvailable DESC")
    List<Theater> findAvailableTheaters();
    
    // Get theaters by availability percentage
    @Query("SELECT t FROM Theater t WHERE (CAST(t.seatsAvailable AS DOUBLE) / CAST(t.seatCapacity AS DOUBLE)) >= :minAvailabilityRatio ORDER BY t.name")
    List<Theater> findTheatersByAvailabilityRatio(double minAvailabilityRatio);
    
    // Check if theater exists with same name and location
    boolean existsByNameIgnoreCaseAndLocationIgnoreCase(String name, String location);
}
