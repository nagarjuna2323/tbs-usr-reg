package com.example.usr_reg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.usr_reg.repository.TheaterRepository;
import com.example.usr_reg.model.Theater;
import com.example.usr_reg.dto.TheaterCreateRequestDto;
import com.example.usr_reg.dto.TheaterCreateResponseDto;
import com.example.usr_reg.dto.TheaterResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TheaterService {
    
    @Autowired
    private TheaterRepository theaterRepository;

    // CREATE THEATER
    public TheaterCreateResponseDto createTheater(TheaterCreateRequestDto dto) {
        // Input validation
        if (dto == null) {
            throw new IllegalArgumentException("Theater data cannot be null");
        }

        // Business rule validation
        if (dto.getSeatsAvailable() > dto.getSeatCapacity()) {
            throw new IllegalArgumentException("Available seats cannot exceed total seat capacity");
        }

        // Check for duplicate theater name at the same location
        List<Theater> existingTheaters = theaterRepository.findByNameAndLocationIgnoreCase(
            dto.getName().trim(), dto.getLocation().trim());
        
        if (!existingTheaters.isEmpty()) {
            throw new IllegalStateException("A theater with this name already exists at this location");
        }

        // Create new theater
        Theater theater = Theater.builder()
                .name(dto.getName().trim())
                .location(dto.getLocation().trim())
                .showTime(dto.getShowTime() != null ? dto.getShowTime().trim() : null)
                .movieTitle(dto.getMovieTitle() != null ? dto.getMovieTitle().trim() : null)
                .seatCapacity(dto.getSeatCapacity())
                .seatsAvailable(dto.getSeatsAvailable())
                .build();

        Theater savedTheater = theaterRepository.save(theater);

        return TheaterCreateResponseDto.builder()
                .message("Theater created successfully")
                .success(true)
                .theaterId(savedTheater.getId())
                .theaterName(savedTheater.getName())
                .location(savedTheater.getLocation())
                .createdAt(savedTheater.getCreatedAt())
                .build();
    }

    // UPDATE THEATER
    public TheaterCreateResponseDto updateTheater(Long id, TheaterCreateRequestDto dto) {
        // Find existing theater
        Optional<Theater> theaterOpt = theaterRepository.findById(id);
        if (theaterOpt.isEmpty()) {
            throw new IllegalArgumentException("Theater not found with ID: " + id);
        }

        Theater theater = theaterOpt.get();

        // Validation
        if (dto.getSeatsAvailable() > dto.getSeatCapacity()) {
            throw new IllegalArgumentException("Available seats cannot exceed total seat capacity");
        }

        // Check for duplicate name/location (excluding current theater)
        List<Theater> duplicates = theaterRepository.findByNameAndLocationIgnoreCase(
            dto.getName().trim(), dto.getLocation().trim());
        
        if (!duplicates.isEmpty() && !duplicates.get(0).getId().equals(id)) {
            throw new IllegalStateException("Another theater with this name already exists at this location");
        }

        // Update theater properties
        theater.setName(dto.getName().trim());
        theater.setLocation(dto.getLocation().trim());
        theater.setShowTime(dto.getShowTime() != null ? dto.getShowTime().trim() : null);
        theater.setMovieTitle(dto.getMovieTitle() != null ? dto.getMovieTitle().trim() : null);
        theater.setSeatCapacity(dto.getSeatCapacity());
        theater.setSeatsAvailable(dto.getSeatsAvailable());

        Theater savedTheater = theaterRepository.save(theater);

        return TheaterCreateResponseDto.builder()
                .message("Theater updated successfully")
                .success(true)
                .theaterId(savedTheater.getId())
                .theaterName(savedTheater.getName())
                .location(savedTheater.getLocation())
                .createdAt(savedTheater.getUpdatedAt())
                .build();
    }

    // DELETE THEATER
    public void deleteTheater(Long id) {
        Optional<Theater> theaterOpt = theaterRepository.findById(id);
        if (theaterOpt.isEmpty()) {
            throw new IllegalArgumentException("Theater not found with ID: " + id);
        }

        theaterRepository.deleteById(id);
    }

    // GET ALL THEATERS (for completeness)
    @Transactional(readOnly = true)
    public List<TheaterResponseDto> getAllTheaters() {
        List<Theater> theaters = theaterRepository.findAll();
        
        return theaters.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // GET THEATER BY ID
    @Transactional(readOnly = true)
    public TheaterResponseDto getTheaterById(Long id) {
        Optional<Theater> theaterOpt = theaterRepository.findById(id);
        if (theaterOpt.isEmpty()) {
            throw new IllegalArgumentException("Theater not found with ID: " + id);
        }
        
        return convertToDto(theaterOpt.get());
    }

    // HELPER METHOD TO CONVERT ENTITY TO DTO
    private TheaterResponseDto convertToDto(Theater theater) {
        double occupancyRate = 0.0;
        String availabilityStatus;
        
        if (theater.getSeatCapacity() > 0) {
            occupancyRate = ((double) (theater.getSeatCapacity() - theater.getSeatsAvailable()) 
                            / theater.getSeatCapacity()) * 100;
        }

        if (theater.getSeatsAvailable() == 0) {
            availabilityStatus = "FULLY_BOOKED";
        } else if (theater.getSeatsAvailable() < (theater.getSeatCapacity() * 0.2)) {
            availabilityStatus = "ALMOST_FULL";
        } else if (theater.getSeatsAvailable() < (theater.getSeatCapacity() * 0.5)) {
            availabilityStatus = "HALF_FULL";
        } else {
            availabilityStatus = "AVAILABLE";
        }

        return TheaterResponseDto.builder()
                .id(theater.getId())
                .name(theater.getName())
                .location(theater.getLocation())
                .showTime(theater.getShowTime())
                .movieTitle(theater.getMovieTitle())
                .seatCapacity(theater.getSeatCapacity())
                .seatsAvailable(theater.getSeatsAvailable())
                .occupancyRate(Math.round(occupancyRate * 100.0) / 100.0)
                .availabilityStatus(availabilityStatus)
                .build();
    }
}
