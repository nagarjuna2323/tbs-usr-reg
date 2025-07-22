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
import jakarta.mail.MessagingException;

@Service
@Transactional  // Class-level transaction management
public class RegistrationService {
    private static final int MAX_USERS = 10;
    private static final String OTP_STATUS_PENDING = "No";
    private static final String OTP_STATUS_VERIFIED = "Yes";
    private static final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService; // <-- add this!

    public void register(RegistrationRequestDto dto, String ipAddress) {
        if (dto == null) throw new IllegalArgumentException("Registration data cannot be null");
        if (ipAddress == null || ipAddress.trim().isEmpty())
            throw new IllegalArgumentException("IP address cannot be null or empty");
        if (userRepository.count() >= MAX_USERS)
            throw new IllegalStateException("Registration limit of " + MAX_USERS + " users reached");
        if (userRepository.findByEmail(dto.getEmail()).isPresent())
            throw new IllegalArgumentException("User with this email already exists");

        String otp = String.format("%06d", secureRandom.nextInt(1000000));
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

        System.out.println("Registering with IP: " + ipAddress);


        try {
            emailService.sendOtpEmail(user.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
        // No need to print OTP anymore!
    }

    public boolean verifyOtp(OtpVerificationDto dto) {
        if (dto == null)
            throw new IllegalArgumentException("OTP verification data cannot be null");

        Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());
        if (userOpt.isEmpty())
            throw new IllegalArgumentException("User not found with email: " + dto.getEmail());

        User user = userOpt.get();

        if (OTP_STATUS_VERIFIED.equalsIgnoreCase(user.getOtpStatus()))
            return true;

        if (user.getOtp().equals(dto.getOtp())) {
            user.setOtpStatus(OTP_STATUS_VERIFIED);
            userRepository.save(user);
            return true;
        }
        return false;
    }

     @Transactional
    public void deleteUserByEmail(String email) {
            Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
    }
    userRepository.delete(userOpt.get());
}
}
