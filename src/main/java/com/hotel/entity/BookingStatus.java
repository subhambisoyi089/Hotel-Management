package com.hotel.entity;

public enum BookingStatus {
    BOOKED("Booked"), CHECKED_IN("Checked-In"), CHECKED_OUT("Checked-Out"), CANCELLED("Cancelled");

    private final String label;

    BookingStatus(String label) { this.label = label; }

    public String getLabel() { return label; }

    /** CSS class suffix, e.g. "checkedin" (used as s-checkedin). */
    public String getCss() { return name().toLowerCase().replace("_", ""); }
}
