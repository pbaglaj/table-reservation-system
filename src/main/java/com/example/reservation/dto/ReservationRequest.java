package com.example.reservation.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ReservationRequest {

    @NotNull
    private Long tableId;

    @NotNull(message = "Date and time are required")
    @Future(message = "Reservation must be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 50, message = "Name is too long")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;
}
