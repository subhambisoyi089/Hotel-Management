# Hotel Management System

A web-based front desk system for a small hotel, built with **Java 17, Spring Boot, JSP/JSTL, Spring Data JPA (Hibernate)** and an **H2** file database. Take bookings, check guests in and out, manage rooms and guests, and print GST invoices.

## Features

- Front desk dashboard: occupancy, free rooms, guests waiting to check in or out, revenue billed this month
- Room board where every room is a key tag coloured by status (available, occupied, maintenance)
- Bookings with date-overlap protection, so a room can't be booked twice for the same nights
- New booking form that lists only the rooms free on the chosen dates, with a live price estimate
- Check in and check out, with extra charges (food, laundry) added at check-out
- Invoices with room charge, extras, GST and a print-friendly layout
- Rooms and guests management with search, edit and delete rules that protect booking history
- Login with BCrypt-hashed password, session handling and CSRF protection on every form

## Requirements

- JDK 17 or newer
- Maven 3.8+ (or an IDE that bundles it: IntelliJ IDEA, Eclipse, VS Code with the Java extension pack)

## Run it

```bash
mvn spring-boot:run
```

Open http://localhost:8080 and sign in:

| Username | Password |
|----------|----------|
| admin    | admin123 |

The database is created in the `data/` folder and ten sample rooms are added on first run.

To build a single runnable file instead:

```bash
mvn clean package
java -jar target/hotel-management.war
```

## Configure

Everything is in `src/main/resources/application.properties`:

- `hotel.name`, `hotel.address`, `hotel.phone`, `hotel.gstin`: shown in the sidebar and on invoices
- `hotel.gst-percent`: GST rate used on bills
- `server.port`: change it if 8080 is busy
- Admin password: set the `ADMIN_PASSWORD` environment variable before the first run

To use MySQL instead of H2, add the MySQL driver to `pom.xml` and change the three `spring.datasource.*` lines.

## Project structure

```
src/main
├── java/com/hotel
│   ├── HotelApplication.java      # entry point
│   ├── config/                    # settings, password encoder, first-run data
│   ├── entity/                    # Room, Guest, Booking, AppUser and their enums
│   ├── repository/                # Spring Data JPA interfaces
│   ├── service/                   # business rules (booking, billing, dashboard)
│   ├── controller/                # request handlers
│   ├── web/                       # login check, CSRF check, shared error handling
│   └── exception/
├── resources
│   ├── application.properties
│   └── static/                    # css and js
└── webapp/WEB-INF
    ├── jsp/                       # pages (dashboard, rooms, guests, bookings, invoice)
    │   └── layout/                # header and footer shared by all pages
    └── tags/                      # small reusable JSP tags (money, status pill, booking buttons)
```

## How a stay works

1. **Booked**: created from New booking. The room stays free until the guest arrives.
2. **Checked-In**: the room becomes Occupied. Allowed from the check-in date onwards.
3. **Checked-Out**: the room becomes Available and the invoice is created. Nights run from check-in to the actual check-out day, minimum one night.
4. **Cancelled**: only a booking that hasn't been checked in can be cancelled.
