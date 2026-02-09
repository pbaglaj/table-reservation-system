package com.example.reservation.service;

import com.example.reservation.dto.ReservationRequest;
import com.example.reservation.model.Reservation;
import com.example.reservation.model.RestaurantTable;
import com.example.reservation.repository.ReservationRepository;
import com.example.reservation.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;

    public ReservationService(ReservationRepository reservationRepository, TableRepository tableRepository) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public void makeReservation(ReservationRequest request) {
        RestaurantTable table =  tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new IllegalArgumentException("Table not found"));

        LocalDateTime endTime = request.getStartTime().plusMinutes(60);

        List<Reservation> conflicts = reservationRepository.findOverlappingReservations(request.getTableId(), request.getStartTime(), endTime);

        if (!conflicts.isEmpty()) {
           throw new IllegalStateException("This table is occupied in selected hours!");
        }

        Reservation reservation = new Reservation();
        reservation.setRestaurantTable(table);
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getStartTime().plusMinutes(60));
        reservation.setCustomerName(request.getName());
        reservation.setCustomerEmail(request.getEmail());

        reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationForTableAndDate(Long tableId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return reservationRepository.findReservationsForTableOnDate(tableId, startOfDay, endOfDay);
    }
}
