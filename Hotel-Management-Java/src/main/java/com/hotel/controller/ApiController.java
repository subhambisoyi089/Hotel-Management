package com.hotel.controller;

import com.hotel.service.BookingService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** JSON used by the New booking page to list free rooms for the chosen dates. */
@RestController
public class ApiController {

    public record RoomOption(Long id, String roomNo, String roomType, BigDecimal price, int capacity) { }

    private final BookingService bookingService;

    public ApiController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/api/available-rooms")
    public ResponseEntity<List<RoomOption>> availableRooms(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            return ResponseEntity.badRequest().body(List.of());
        }
        List<RoomOption> options = bookingService.availableRooms(checkIn, checkOut).stream()
                .map(r -> new RoomOption(r.getId(), r.getRoomNo(), r.getType().getLabel(),
                        r.getPricePerNight(), r.getCapacity()))
                .toList();
        return ResponseEntity.ok(options);
    }
}
