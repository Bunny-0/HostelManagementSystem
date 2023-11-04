package com.example.hostelManagementTool.Repository;

import com.example.hostelManagementTool.Model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Integer> {
}
