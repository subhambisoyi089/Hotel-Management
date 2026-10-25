package com.hotel.service;

import java.math.BigDecimal;

/** The amounts on an invoice. Plain getters so the JSP pages can read it. */
public class Bill {

    private final long nights;
    private final BigDecimal roomCharge;
    private final BigDecimal extra;
    private final BigDecimal subtotal;
    private final BigDecimal gst;
    private final BigDecimal total;

    public Bill(long nights, BigDecimal roomCharge, BigDecimal extra,
                BigDecimal subtotal, BigDecimal gst, BigDecimal total) {
        this.nights = nights;
        this.roomCharge = roomCharge;
        this.extra = extra;
        this.subtotal = subtotal;
        this.gst = gst;
        this.total = total;
    }

    public long getNights() { return nights; }
    public BigDecimal getRoomCharge() { return roomCharge; }
    public BigDecimal getExtra() { return extra; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getGst() { return gst; }
    public BigDecimal getTotal() { return total; }
    public boolean isHasExtra() { return extra != null && extra.signum() > 0; }
}
