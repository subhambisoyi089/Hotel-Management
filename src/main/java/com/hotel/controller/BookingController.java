package com.hotel.controller;

import com.hotel.entity.Booking;
import com.hotel.entity.BookingStatus;
import com.hotel.entity.Guest;
import com.hotel.exception.BusinessException;
import com.hotel.repository.GuestRepository;
import com.hotel.service.BillingService;
import com.hotel.service.BookingService;
import com.hotel.service.NewBooking;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final BillingService billingService;
    private final GuestRepository guestRepository;

    public BookingController(BookingService bookingService, BillingService billingService,
                             GuestRepository guestRepository) {
        this.bookingService = bookingService;
        this.billingService = billingService;
        this.guestRepository = guestRepository;
    }

    @GetMapping("/bookings")
    public String list(@RequestParam(required = false) BookingStatus status, Model model) {
        model.addAttribute("bookings", bookingService.list(status));
        model.addAttribute("selected", status);
        model.addAttribute("statuses", BookingStatus.values());
        return "bookings";
    }

    @GetMapping("/bookings/new")
    public String newForm(@RequestParam(required = false, defaultValue = "") String room, Model model) {
        model.addAttribute("guests", guestRepository.findAllByOrderByNameAsc());
        model.addAttribute("form", Map.of("roomId", room));
        return "new-booking";
    }

    @PostMapping("/bookings/new")
    public String create(@RequestParam Map<String, String> form,
                         @RequestParam(required = false) String guestId,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
                         @RequestParam(required = false) Long roomId,
                         @RequestParam(required = false) Integer numGuests,
                         RedirectAttributes flash, Model model) {
        NewBooking request = new NewBooking(guestId, form.get("newName"), form.get("newPhone"),
                form.get("newEmail"), form.get("newIdProof"), checkIn, checkOut, roomId, numGuests);
        try {
            Booking booking = bookingService.create(request);
            flash.addFlashAttribute("success",
                    "Booking " + booking.getCode() + " saved for room " + booking.getRoom().getRoomNo() + ".");
            return "redirect:/bookings";
        } catch (BusinessException ex) {
            // Show the form again with what the user typed.
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("guests", guestRepository.findAllByOrderByNameAsc());
            model.addAttribute("form", form);
            return "new-booking";
        }
    }

    @PostMapping("/bookings/{id}/checkin")
    public String checkIn(@PathVariable Long id, RedirectAttributes flash) {
        Booking b = bookingService.checkIn(id);
        Guest guest = b.getGuest();
        flash.addFlashAttribute("success", guest.getName() + " checked in to room " + b.getRoom().getRoomNo() + ".");
        return "redirect:/bookings";
    }

    @PostMapping("/bookings/{id}/checkout")
    public String checkOut(@PathVariable Long id,
                           @RequestParam(required = false) String extraCharges,
                           @RequestParam(required = false) String extraNote,
                           RedirectAttributes flash) {
        Booking b = bookingService.checkOut(id, extraCharges, extraNote);
        flash.addFlashAttribute("success", b.getGuest().getName() + " checked out of room " + b.getRoom().getRoomNo() + ".");
        return "redirect:/bookings/" + id + "/invoice";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancel(@PathVariable Long id, RedirectAttributes flash) {
        bookingService.cancel(id);
        flash.addFlashAttribute("success", "Booking cancelled.");
        return "redirect:/bookings";
    }

    @GetMapping("/bookings/{id}/invoice")
    public String invoice(@PathVariable Long id, Model model) {
        Booking b = bookingService.get(id);
        if (!b.isCheckedOut()) {
            throw new BusinessException("An invoice is available after check-out.");
        }
        model.addAttribute("b", b);
        model.addAttribute("bill", billingService.calculate(b));
        return "invoice";
    }
}
