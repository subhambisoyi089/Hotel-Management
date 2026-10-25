package com.hotel.service;

import com.hotel.entity.Booking;
import com.hotel.entity.BookingStatus;
import com.hotel.entity.Guest;
import com.hotel.entity.Room;
import com.hotel.entity.RoomStatus;
import com.hotel.exception.BusinessException;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.GuestRepository;
import com.hotel.repository.RoomRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    /** Bookings in these states keep a room busy for their dates. */
    private static final List<BookingStatus> ACTIVE = List.of(BookingStatus.BOOKED, BookingStatus.CHECKED_IN);

    private final BookingRepository bookings;
    private final RoomRepository rooms;
    private final GuestRepository guests;
    private final BillingService billing;

    public BookingService(BookingRepository bookings, RoomRepository rooms,
                          GuestRepository guests, BillingService billing) {
        this.bookings = bookings;
        this.rooms = rooms;
        this.guests = guests;
        this.billing = billing;
    }

    @Transactional(readOnly = true)
    public List<Booking> list(BookingStatus status) {
        return status == null
                ? bookings.findAllByOrderByCheckInDescIdDesc()
                : bookings.findByStatusOrderByCheckInDescIdDesc(status);
    }

    @Transactional(readOnly = true)
    public Booking get(Long id) {
        return bookings.findById(id).orElseThrow(() -> new BusinessException("That booking was not found."));
    }

    /** Rooms that are not under maintenance and have no booking overlapping these dates. */
    @Transactional(readOnly = true)
    public List<Room> availableRooms(LocalDate checkIn, LocalDate checkOut) {
        return rooms.findAllByOrderByRoomNoAsc().stream()
                .filter(r -> r.getStatus() != RoomStatus.MAINTENANCE)
                .filter(r -> bookings.countOverlapping(r.getId(), ACTIVE, checkIn, checkOut) == 0)
                .toList();
    }

    @Transactional
    public Booking create(NewBooking form) {
        LocalDate in = form.checkIn();
        LocalDate out = form.checkOut();
        if (in == null || out == null) {
            throw new BusinessException("Choose valid check-in and check-out dates.");
        }
        if (in.isBefore(LocalDate.now())) {
            throw new BusinessException("Check-in can't be in the past.");
        }
        if (!out.isAfter(in)) {
            throw new BusinessException("Check-out must be after check-in.");
        }

        Room room = form.roomId() == null ? null : rooms.findById(form.roomId()).orElse(null);
        if (room == null || room.getStatus() == RoomStatus.MAINTENANCE) {
            throw new BusinessException("Pick an available room.");
        }
        int people = form.numGuests() == null ? 0 : form.numGuests();
        if (people < 1 || people > room.getCapacity()) {
            throw new BusinessException("Room " + room.getRoomNo() + " sleeps " + room.getCapacity() + ".");
        }
        if (bookings.countOverlapping(room.getId(), ACTIVE, in, out) > 0) {
            throw new BusinessException("That room is already booked for those dates. Pick another room or change the dates.");
        }

        Guest guest = resolveGuest(form);
        return bookings.save(new Booking(guest, room, in, out, people));
    }

    private Guest resolveGuest(NewBooking form) {
        if ("new".equals(form.guestId())) {
            if (form.newName() == null || form.newName().isBlank() || !GuestService.isValidPhone(form.newPhone())) {
                throw new BusinessException("Enter the new guest's name and a valid phone number.");
            }
            Guest guest = new Guest();
            guest.setName(form.newName().trim());
            guest.setPhone(form.newPhone().trim());
            guest.setEmail(blankToNull(form.newEmail()));
            guest.setIdProof(blankToNull(form.newIdProof()));
            return guests.save(guest);
        }
        try {
            return guests.findById(Long.valueOf(form.guestId()))
                    .orElseThrow(() -> new BusinessException("Choose a guest or add a new one."));
        } catch (NumberFormatException e) {
            throw new BusinessException("Choose a guest or add a new one.");
        }
    }

    @Transactional
    public Booking checkIn(Long id) {
        Booking b = get(id);
        Room room = b.getRoom();
        if (b.getStatus() != BookingStatus.BOOKED) {
            throw new BusinessException("Only a confirmed booking can be checked in.");
        }
        if (b.getCheckIn().isAfter(LocalDate.now())) {
            throw new BusinessException("This booking starts on " + b.getCheckInText() + ". You can check in from that day.");
        }
        if (room.getStatus() == RoomStatus.OCCUPIED) {
            throw new BusinessException("Room " + room.getRoomNo() + " is still occupied. Check out the current guest first.");
        }
        if (room.getStatus() == RoomStatus.MAINTENANCE) {
            throw new BusinessException("Room " + room.getRoomNo() + " is under maintenance. Book another room for this guest.");
        }
        b.setStatus(BookingStatus.CHECKED_IN);
        room.setStatus(RoomStatus.OCCUPIED);
        rooms.save(room);
        return bookings.save(b);
    }

    @Transactional
    public Booking checkOut(Long id, String extraText, String note) {
        Booking b = get(id);
        if (b.getStatus() != BookingStatus.CHECKED_IN) {
            throw new BusinessException("Only a guest who is checked in can be checked out.");
        }
        BigDecimal extra = parseAmount(extraText);

        LocalDate today = LocalDate.now();
        // The bill runs until the day the guest actually leaves.
        b.setCheckOut(today.isBefore(b.getCheckIn()) ? b.getCheckIn() : today);
        b.setExtraCharges(extra);
        b.setExtraNote(note == null || note.isBlank() ? null : trimTo(note.trim(), 120));
        b.setTotalAmount(billing.calculate(b).getTotal());
        b.setCheckedOutOn(today);
        b.setStatus(BookingStatus.CHECKED_OUT);

        Room room = b.getRoom();
        room.setStatus(RoomStatus.AVAILABLE);
        rooms.save(room);
        return bookings.save(b);
    }

    @Transactional
    public void cancel(Long id) {
        Booking b = get(id);
        if (b.getStatus() != BookingStatus.BOOKED) {
            throw new BusinessException("Only a confirmed booking can be cancelled.");
        }
        b.setStatus(BookingStatus.CANCELLED);
        bookings.save(b);
    }

    private static BigDecimal parseAmount(String text) {
        if (text == null || text.isBlank()) {
            return BigDecimal.ZERO.setScale(2);
        }
        try {
            BigDecimal value = new BigDecimal(text.trim());
            if (value.signum() >= 0) {
                return value.setScale(2, RoundingMode.HALF_UP);
            }
        } catch (NumberFormatException ignored) {
            // falls through to the error below
        }
        throw new BusinessException("Extra charges must be a number, 0 or more.");
    }

    private static String trimTo(String text, int max) {
        return text.length() <= max ? text : text.substring(0, max);
    }

    private static String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
