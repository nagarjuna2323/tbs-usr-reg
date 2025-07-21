package com.example.usr_reg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.example.usr_reg.service.TheaterService;
import com.example.usr_reg.dto.TheaterCreateRequestDto;
import com.example.usr_reg.dto.TheaterCreateResponseDto;
import com.example.usr_reg.dto.TheaterResponseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/theaters")
public class TheaterController {
    
    @Autowired
    private TheaterService theaterService;

    // CREATE THEATER
    @PostMapping
    public ResponseEntity<TheaterCreateResponseDto> createTheater(
            @RequestBody @Valid TheaterCreateRequestDto dto) {
        try {
            TheaterCreateResponseDto response = theaterService.createTheater(dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                TheaterCreateResponseDto.builder()
                    .message(e.getMessage())
                    .success(false)
                    .build()
            );
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(
                TheaterCreateResponseDto.builder()
                    .message(e.getMessage())
                    .success(false)
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                TheaterCreateResponseDto.builder()
                    .message("Error creating theater: " + e.getMessage())
                    .success(false)
                    .build()
            );
        }
    }

    // UPDATE THEATER
    @PutMapping("/{id}")
    public ResponseEntity<TheaterCreateResponseDto> updateTheater(
            @PathVariable Long id,
            @RequestBody @Valid TheaterCreateRequestDto dto) {
        try {
            TheaterCreateResponseDto response = theaterService.updateTheater(id, dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                TheaterCreateResponseDto.builder()
                    .message(e.getMessage())
                    .success(false)
                    .build()
            );
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(
                TheaterCreateResponseDto.builder()
                    .message(e.getMessage())
                    .success(false)
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                TheaterCreateResponseDto.builder()
                    .message("Error updating theater: " + e.getMessage())
                    .success(false)
                    .build()
            );
        }
    }

    // DELETE THEATER
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTheater(@PathVariable Long id) {
        try {
            theaterService.deleteTheater(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Theater deleted successfully");
            response.put("success", true);
            response.put("deletedTheaterId", id);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error deleting theater: " + e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    // GET ALL THEATERS (Optional - for viewing)
    @GetMapping
    public ResponseEntity<List<TheaterResponseDto>> getAllTheaters() {
        try {
            List<TheaterResponseDto> theaters = theaterService.getAllTheaters();
            return ResponseEntity.ok(theaters);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // GET THEATER BY ID (Optional - for viewing)
    @GetMapping("/{id}")
    public ResponseEntity<TheaterResponseDto> getTheaterById(@PathVariable Long id) {
        try {
            TheaterResponseDto theater = theaterService.getTheaterById(id);
            return ResponseEntity.ok(theater);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
