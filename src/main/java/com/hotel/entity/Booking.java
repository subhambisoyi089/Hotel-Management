package com.hotel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Table(name = "bookings")
public class Booking {

    private static final DateTimeFormatter DISPLAY = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private int numGuests = 1;

    /** Price per night at the time of booking, so later price changes don't alter old bills. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal rate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status = BookingStatus.BOOKED;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal extraCharges = BigDecimal.ZERO.setScale(2);

    @Column(length = 120)
    private String extraNote;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalAmount;

    private LocalDate checkedOutOn;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Booking() { }

    public Booking(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut, int numGuests) {
        this.guest = guest;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numGuests = numGuests;
        this.rate = room.getPricePerNight();
    }

    public Long getId() { return id; }
    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }
    public int getNumGuests() { return numGuests; }
    public BigDecimal getRate() { return rate; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public BigDecimal getExtraCharges() { return extraCharges; }
    public void setExtraCharges(BigDecimal extraCharges) { this.extraCharges = extraCharges; }
    public String getExtraNote() { return extraNote; }
    public void setExtraNote(String extraNote) { this.extraNote = extraNote; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public LocalDate getCheckedOutOn() { return checkedOutOn; }
    public void setCheckedOutOn(LocalDate checkedOutOn) { this.checkedOutOn = checkedOutOn; }

    // ---- helpers used by the JSP pages (JSP's fmt:formatDate can't format LocalDate) ----
    public String getCode() { return String.format("BK%04d", id); }
    public String getInvoiceNo() { return String.format("INV-%04d", id); }
    public String getCheckInText() { return checkIn == null ? "-" : checkIn.format(DISPLAY); }
    public String getCheckOutText() { return checkOut == null ? "-" : checkOut.format(DISPLAY); }
    public String getCheckedOutOnText() { return checkedOutOn == null ? "-" : checkedOutOn.format(DISPLAY); }

    public boolean isBooked() { return status == BookingStatus.BOOKED; }
    public boolean isCheckedIn() { return status == BookingStatus.CHECKED_IN; }
    public boolean isCheckedOut() { return status == BookingStatus.CHECKED_OUT; }
}
