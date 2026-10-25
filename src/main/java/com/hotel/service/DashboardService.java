package com.hotel.service;

import com.hotel.entity.Booking;
import com.hotel.entity.BookingStatus;
import com.hotel.entity.Room;
import com.hotel.entity.RoomStatus;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.RoomRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final RoomRepository rooms;
    private final BookingRepository bookings;

    public DashboardService(RoomRepository rooms, BookingRepository bookings) {
        this.rooms = rooms;
        this.bookings = bookings;
    }

    @Transactional(readOnly = true)
    public DashboardData build() {
        LocalDate today = LocalDate.now();
        List<Room> allRooms = rooms.findAllByOrderByRoomNoAsc();

        Map<Long, String> guestByRoom = new HashMap<>();
        for (Booking b : bookings.findByStatus(BookingStatus.CHECKED_IN)) {
            guestByRoom.put(b.getRoom().getId(), b.getGuest().getName());
        }

        List<Booking> arrivals = bookings
                .findByStatusAndCheckInLessThanEqualOrderByCheckInAsc(BookingStatus.BOOKED, today);
        List<Booking> departures = bookings
                .findByStatusAndCheckOutLessThanEqualOrderByCheckOutAsc(BookingStatus.CHECKED_IN, today);

        BigDecimal revenue = bookings.revenueBetween(BookingStatus.CHECKED_OUT,
                today.withDayOfMonth(1), today.withDayOfMonth(today.lengthOfMonth()));
        if (revenue == null) {
            revenue = BigDecimal.ZERO;
        }

        int occupied = (int) allRooms.stream().filter(r -> r.getStatus() == RoomStatus.OCCUPIED).count();
        int maintenance = (int) allRooms.stream().filter(r -> r.getStatus() == RoomStatus.MAINTENANCE).count();
        return new DashboardData(allRooms, guestByRoom, arrivals, departures, revenue,
                allRooms.size(), occupied, maintenance);
    }
}
