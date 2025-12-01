package com.anas.kos_agus_apik.repository;

import com.anas.kos_agus_apik.entity.Room;
import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.entity.enum_class.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

}
