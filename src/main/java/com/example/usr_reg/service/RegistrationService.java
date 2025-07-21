package com.example.usr_reg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.usr_reg.repository.UserRepository;
import com.example.usr_reg.model.User;
import com.example.usr_reg.dto.RegistrationRequestDto;
import com.example.usr_reg.dto.OtpVerificationDto;

import java.time.LocalDateTime;
import java.util.Optional;
import java.security.SecureRandom;

@Service
@Transactional  // Class-level transaction management
public class RegistrationService {
    
    private static final int MAX_USERS = 10;
    private static final String OTP_STATUS_PENDING = "No";
    private static final String OTP_STATUS_VERIFIED = "Yes";
    private static final SecureRandom secureRandom = new SecureRandom();
    
    @Autowired
    private UserRepository userRepository;

    public void register(RegistrationRequestDto dto, String ipAddress) {
        // Input validation
        if (dto == null) {
            throw new IllegalArgumentException("Registration data cannot be null");
        }
        if (ipAddress == null || ipAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("IP address cannot be null or empty");
        }
        
        // Business rule validation
        if (userRepository.count() >= MAX_USERS) {
            throw new IllegalStateException("Registration limit of " + MAX_USERS + " users reached");
        }
        
        // Check for duplicate email
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        
        // Generate secure OTP
        String otp = String.format("%06d", secureRandom.nextInt(1000000));
        
        // Create and save user
        User user = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .registrationIp(ipAddress)
                .otp(otp)
                .otpStatus(OTP_STATUS_PENDING)
                .registeredAt(LocalDateTime.now())
                .build();
        
        userRepository.save(user);
        
        // TODO: Replace with actual email/SMS service
        System.out.println("DEBUG: OTP for " + user.getEmail() + " is " + otp);
    }

    public boolean verifyOtp(OtpVerificationDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("OTP verification data cannot be null");
        }
        
        Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + dto.getEmail());
        }
        
        User user = userOpt.get();
        
        // Already verified
        if (OTP_STATUS_VERIFIED.equalsIgnoreCase(user.getOtpStatus())) {
            return true;
        }
        
        // Verify OTP
        if (user.getOtp().equals(dto.getOtp())) {
            user.setOtpStatus(OTP_STATUS_VERIFIED);
            userRepository.save(user);
            return true;
        }
        
        return false;
    }
}
