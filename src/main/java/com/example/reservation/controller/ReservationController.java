package com.example.reservation.controller;

import com.example.reservation.model.Reservation;
import com.example.reservation.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reserve/{tableId}")
    public String showForm(
            @PathVariable Long tableId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        if (date == null) {
            date = LocalDate.now();
        }

        List<Reservation> reservations = reservationService.getReservationForTableAndDate(tableId, date);

        Set<Integer> reservedHours = reservations.stream()
                        .map(r -> r.getStartTime().getHour())
                        .collect(Collectors.toSet());

        model.addAttribute("tableId", tableId);
        model.addAttribute("selectedDate", date);
        model.addAttribute("reservedHours", reservedHours);

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
