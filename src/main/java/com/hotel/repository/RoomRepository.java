package com.hotel.repository;

import com.hotel.entity.Room;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAllByOrderByRoomNoAsc();

    Optional<Room> findByRoomNo(String roomNo);
}
