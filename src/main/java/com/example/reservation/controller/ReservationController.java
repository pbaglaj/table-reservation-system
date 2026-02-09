package com.example.reservation.controller;

import com.example.reservation.dto.ReservationRequest;
import com.example.reservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

        if (date == null) date =  LocalDate.now();

        ReservationRequest form = new ReservationRequest();
        form.setTableId(tableId);

        model.addAttribute("reservationForm", form);

        prepareModelForView(tableId, date, model);

        return "reservation-form";
    }

    @PostMapping("/reserve")
    public String submitReservation(
            @Valid @ModelAttribute("reservationForm")ReservationRequest request,
            BindingResult bindingResult,
            Model model) {

        LocalDate dateForView = (request.getStartTime() != null)
                ? request.getStartTime().toLocalDate()
                : LocalDate.now();

        if(bindingResult.hasErrors()){
            prepareModelForView(request.getTableId(), dateForView, model);
            return "reservation-form";
        }

        try {
            reservationService.makeReservation(request);
            return "redirect:/tables?success";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            prepareModelForView(request.getTableId(), dateForView, model);
            return "reservation-form";
        }
    }

    private void prepareModelForView(Long tableId, LocalDate date, Model model) {
        model.addAttribute("tableId", tableId);
        model.addAttribute("selectedDate", date);
        model.addAttribute("reservedHours",
                reservationService.getReservationForTableAndDate(tableId, date)
                        .stream().map(r -> r.getStartTime().getHour()).collect(Collectors.toSet())
        );
    }
}
