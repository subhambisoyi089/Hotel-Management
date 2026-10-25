package com.hotel.service;

import com.hotel.entity.Booking;
import com.hotel.entity.Room;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** Everything the front desk page shows. Plain getters so the JSP can read it. */
public class DashboardData {

    private final List<Room> rooms;
    private final Map<Long, String> guestByRoom;
    private final List<Booking> arrivals;
    private final List<Booking> departures;
    private final BigDecimal revenue;
    private final int total;
    private final int occupied;
    private final int maintenance;

    public DashboardData(List<Room> rooms, Map<Long, String> guestByRoom, List<Booking> arrivals,
                         List<Booking> departures, BigDecimal revenue, int total, int occupied, int maintenance) {
        this.rooms = rooms;
        this.guestByRoom = guestByRoom;
        this.arrivals = arrivals;
        this.departures = departures;
        this.revenue = revenue;
        this.total = total;
        this.occupied = occupied;
        this.maintenance = maintenance;
    }

    public List<Room> getRooms() { return rooms; }
    public Map<Long, String> getGuestByRoom() { return guestByRoom; }
    public List<Booking> getArrivals() { return arrivals; }
    public List<Booking> getDepartures() { return departures; }
    public BigDecimal getRevenue() { return revenue; }
    public int getTotal() { return total; }
    public int getOccupied() { return occupied; }
    public int getMaintenance() { return maintenance; }
    public int getAvailable() { return total - occupied - maintenance; }
    public int getOccupancy() { return total == 0 ? 0 : Math.round(occupied * 100f / total); }
}
