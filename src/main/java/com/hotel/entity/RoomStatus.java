package com.hotel.entity;

public enum RoomStatus {
    AVAILABLE("Available"), OCCUPIED("Occupied"), MAINTENANCE("Maintenance");

    private final String label;

    RoomStatus(String label) { this.label = label; }

    public String getLabel() { return label; }

    /** CSS class suffix, e.g. "available" (used as s-available). */
    public String getCss() { return name().toLowerCase().replace("_", ""); }
}
