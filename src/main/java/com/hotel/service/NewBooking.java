package com.hotel.service;

import java.time.LocalDate;

/** Values from the "New booking" form. guestId is a guest's id, or the word "new". */
public record NewBooking(String guestId, String newName, String newPhone, String newEmail, String newIdProof,
                         LocalDate checkIn, LocalDate checkOut, Long roomId, Integer numGuests) {
}
