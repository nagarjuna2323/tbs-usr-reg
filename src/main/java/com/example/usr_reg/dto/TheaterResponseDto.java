package com.example.usr_reg.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class TheaterResponseDto {
    private Long id;
    private String name;
    private String location;
    private String showTime;
    private String movieTitle;
    private Integer seatCapacity;
    private Integer seatsAvailable;
    private Double occupancyRate;
    private String availabilityStatus;
}
