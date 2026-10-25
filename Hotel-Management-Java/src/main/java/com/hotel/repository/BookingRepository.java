package com.hotel.repository;

import com.hotel.entity.Booking;
import com.hotel.entity.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByOrderByCheckInDescIdDesc();

    List<Booking> findByStatusOrderByCheckInDescIdDesc(BookingStatus status);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByStatusAndCheckInLessThanEqualOrderByCheckInAsc(BookingStatus status, LocalDate date);

    List<Booking> findByStatusAndCheckOutLessThanEqualOrderByCheckOutAsc(BookingStatus status, LocalDate date);

    long countByRoomId(Long roomId);

    long countByGuestId(Long guestId);

    /** How many active bookings of this room overlap the nights [checkIn, checkOut). */
    @Query("""
            select count(b) from Booking b
            where b.room.id = :roomId
              and b.status in :active
              and b.checkIn < :checkOut
              and b.checkOut > :checkIn
            """)
    long countOverlapping(@Param("roomId") Long roomId,
                          @Param("active") Collection<BookingStatus> active,
                          @Param("checkIn") LocalDate checkIn,
                          @Param("checkOut") LocalDate checkOut);

    /** Sum of bills for stays checked out between two dates (null when there are none). */
    @Query("""
            select sum(b.totalAmount) from Booking b
            where b.status = :status
              and b.checkedOutOn >= :from
              and b.checkedOutOn <= :to
            """)
    BigDecimal revenueBetween(@Param("status") BookingStatus status,
                              @Param("from") LocalDate from,
                              @Param("to") LocalDate to);
}
