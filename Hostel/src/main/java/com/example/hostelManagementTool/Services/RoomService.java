package com.example.hostelManagementTool.Services;

import com.example.hostelManagementTool.Model.Hostel;
import com.example.hostelManagementTool.Model.Room;
import com.example.hostelManagementTool.Repository.HostelRepository;
import com.example.hostelManagementTool.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {
    @Autowired
    HostelRepository hostelRepository;
    @Autowired
    RoomRepository roomRepository;

    public Room addRoom(Room room){
        Hostel hostel=hostelRepository.findByType(room.getHostel().getType());
        System.out.println("data type is "+room.getHostel().getType().getClass());
        room.setHostel(hostel);
        return roomRepository.save(room);
    }

}
