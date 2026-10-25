package com.hotel.service;

import com.hotel.config.HotelProperties;
import com.hotel.entity.Booking;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

@Service
public class BillingService {

    private final HotelProperties hotel;

    public BillingService(HotelProperties hotel) {
        this.hotel = hotel;
    }

    /** Room charge (minimum one night) + extra charges + GST. */
    public Bill calculate(Booking b) {
        long nights = Math.max(ChronoUnit.DAYS.between(b.getCheckIn(), b.getCheckOut()), 1);
        BigDecimal roomCharge = b.getRate().multiply(BigDecimal.valueOf(nights)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal extra = b.getExtraCharges() == null ? BigDecimal.ZERO.setScale(2) : b.getExtraCharges();
        BigDecimal subtotal = roomCharge.add(extra);
        BigDecimal gst = subtotal.multiply(BigDecimal.valueOf(hotel.getGstPercent()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return new Bill(nights, roomCharge, extra, subtotal, gst, subtotal.add(gst));
    }
}
