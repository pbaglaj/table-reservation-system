package com.example.reservation.controller;

import com.example.reservation.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reserve/{tableId}")
    public String showForm(@PathVariable Long tableId, Model model) {
        model.addAttribute("tableId", tableId);
        return "reservation-form";
    }

    @PostMapping("/reserve")
    public String submitReservation(
            @RequestParam Long tableId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam String name,
            @RequestParam String email,
            Model model) {
        try {
            reservationService.makeReservation(tableId, startTime, name, email);
            return "redirect:/tables?success";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tableId", tableId);
            return "reservation-form";
        }
    }
}
