package com.hotel.service;

import com.hotel.entity.Room;
import com.hotel.entity.RoomStatus;
import com.hotel.entity.RoomType;
import com.hotel.exception.BusinessException;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.RoomRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {

    private final RoomRepository rooms;
    private final BookingRepository bookings;

    public RoomService(RoomRepository rooms, BookingRepository bookings) {
        this.rooms = rooms;
        this.bookings = bookings;
    }

    @Transactional(readOnly = true)
    public List<Room> findAll() {
        return rooms.findAllByOrderByRoomNoAsc();
    }

    /** Adds a room (id == null) or updates an existing one. */
    @Transactional
    public Room save(Long id, String roomNo, RoomType type, BigDecimal price, Integer capacity, RoomStatus status) {
        String no = roomNo == null ? "" : roomNo.trim();
        if (no.isEmpty() || type == null || price == null || price.signum() <= 0
                || capacity == null || capacity < 1) {
            throw new BusinessException("Fill in every room field with valid values.");
        }

        Room room = (id == null)
                ? new Room()
                : rooms.findById(id).orElseThrow(() -> new BusinessException("That room no longer exists."));

        Optional<Room> sameNumber = rooms.findByRoomNo(no);
        if (sameNumber.isPresent() && !sameNumber.get().getId().equals(room.getId())) {
            throw new BusinessException("Room " + no + " already exists.");
        }

        // Only Available or Maintenance can be chosen. Occupied is set by check-in and cleared by check-out.
        RoomStatus newStatus = (status == RoomStatus.MAINTENANCE) ? RoomStatus.MAINTENANCE : RoomStatus.AVAILABLE;
        if (room.getStatus() == RoomStatus.OCCUPIED) {
            newStatus = RoomStatus.OCCUPIED;
        }

        room.setRoomNo(no);
        room.setType(type);
        room.setPricePerNight(price.setScale(2, java.math.RoundingMode.HALF_UP));
        room.setCapacity(capacity);
        room.setStatus(newStatus);
        return rooms.save(room);
    }

    @Transactional
    public void delete(Long id) {
        if (bookings.countByRoomId(id) > 0) {
            throw new BusinessException("This room has booking history. Set it to Maintenance instead of deleting it.");
        }
        rooms.deleteById(id);
    }
}
