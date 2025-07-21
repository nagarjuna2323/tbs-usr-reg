package com.example.usr_reg.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class TheaterCreateRequestDto {
    @NotBlank(message = "Theater name is required")
    @Size(max = 100, message = "Theater name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Location is required")
    @Size(max = 200, message = "Location cannot exceed 200 characters")
    private String location;

    @Size(max = 50, message = "Show time cannot exceed 50 characters")
    private String showTime;

    @Size(max = 200, message = "Movie title cannot exceed 200 characters")
    private String movieTitle;

    @NotNull(message = "Seat capacity is required")
    @Min(value = 1, message = "Seat capacity must be at least 1")
    @Max(value = 1000, message = "Seat capacity cannot exceed 1000")
    private Integer seatCapacity;

    @NotNull(message = "Available seats is required")
    @Min(value = 0, message = "Available seats cannot be negative")
    private Integer seatsAvailable;
}
