package com.example.usr_reg.dto;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class TheaterCreateResponseDto {
    private String message;
    private boolean success;
    private Long theaterId;
    private String theaterName;
    private String location;
    private LocalDateTime createdAt;
}
