package com.hotel.repository;

import com.hotel.entity.Guest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    List<Guest> findAllByOrderByNameAsc();

    List<Guest> findByNameContainingIgnoreCaseOrPhoneContainingOrderByNameAsc(String name, String phone);
}
