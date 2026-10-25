package com.hotel.service;

import com.hotel.entity.Guest;
import com.hotel.exception.BusinessException;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.GuestRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuestService {

    private final GuestRepository guests;
    private final BookingRepository bookings;

    public GuestService(GuestRepository guests, BookingRepository bookings) {
        this.guests = guests;
        this.bookings = bookings;
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.replaceAll("\\D", "").length() >= 7;
    }

    @Transactional(readOnly = true)
    public List<Guest> search(String q) {
        if (q == null || q.isBlank()) {
            return guests.findAllByOrderByNameAsc();
        }
        String term = q.trim();
        return guests.findByNameContainingIgnoreCaseOrPhoneContainingOrderByNameAsc(term, term);
    }

    /** Number of bookings for each guest, keyed by guest id. */
    @Transactional(readOnly = true)
    public Map<Long, Long> stayCounts(List<Guest> list) {
        Map<Long, Long> counts = new HashMap<>();
        for (Guest g : list) {
            counts.put(g.getId(), bookings.countByGuestId(g.getId()));
        }
        return counts;
    }

    @Transactional
    public Guest save(Long id, String name, String phone, String email, String idProof, String address) {
        if (name == null || name.isBlank() || !isValidPhone(phone)) {
            throw new BusinessException("Enter the guest's name and a valid phone number.");
        }
        Guest guest = (id == null)
                ? new Guest()
                : guests.findById(id).orElseThrow(() -> new BusinessException("That guest no longer exists."));
        guest.setName(name.trim());
        guest.setPhone(phone.trim());
        guest.setEmail(clean(email));
        guest.setIdProof(clean(idProof));
        guest.setAddress(clean(address));
        return guests.save(guest);
    }

    @Transactional
    public void delete(Long id) {
        if (bookings.countByGuestId(id) > 0) {
            throw new BusinessException("This guest has booking history, so they can't be deleted.");
        }
        guests.deleteById(id);
    }

    private static String clean(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
