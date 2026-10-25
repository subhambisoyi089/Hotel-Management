package com.hotel.config;

import com.hotel.entity.AppUser;
import com.hotel.entity.Room;
import com.hotel.entity.RoomType;
import com.hotel.repository.RoomRepository;
import com.hotel.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** On first run, creates the admin login and a few sample rooms. */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository users;
    private final RoomRepository rooms;
    private final PasswordEncoder encoder;
    private final String adminPassword;

    public DataSeeder(UserRepository users, RoomRepository rooms, PasswordEncoder encoder,
                      @Value("${app.admin.password}") String adminPassword) {
        this.users = users;
        this.rooms = rooms;
        this.encoder = encoder;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (users.count() == 0) {
            users.save(new AppUser("admin", encoder.encode(adminPassword)));
        }
        if (rooms.count() == 0) {
            rooms.saveAll(List.of(
                    new Room("101", RoomType.STANDARD, new BigDecimal("1800.00"), 2),
                    new Room("102", RoomType.STANDARD, new BigDecimal("1800.00"), 2),
                    new Room("103", RoomType.STANDARD, new BigDecimal("1800.00"), 2),
                    new Room("201", RoomType.DELUXE, new BigDecimal("2800.00"), 3),
                    new Room("202", RoomType.DELUXE, new BigDecimal("2800.00"), 3),
                    new Room("203", RoomType.DELUXE, new BigDecimal("2800.00"), 3),
                    new Room("204", RoomType.FAMILY, new BigDecimal("3800.00"), 5),
                    new Room("301", RoomType.SUITE, new BigDecimal("5200.00"), 4),
                    new Room("302", RoomType.SUITE, new BigDecimal("5200.00"), 4),
                    new Room("303", RoomType.FAMILY, new BigDecimal("3800.00"), 5)));
        }
    }
}
