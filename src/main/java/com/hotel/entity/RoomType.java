package com.hotel.entity;

public enum RoomType {
    STANDARD("Standard"), DELUXE("Deluxe"), SUITE("Suite"), FAMILY("Family");

    private final String label;

    RoomType(String label) { this.label = label; }

    public String getLabel() { return label; }
}
