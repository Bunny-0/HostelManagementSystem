package com.example.hostelManagementTool.Services;

import com.example.hostelManagementTool.Model.Bed;
import com.example.hostelManagementTool.Model.Hostel;
import com.example.hostelManagementTool.Model.Room;
import com.example.hostelManagementTool.Repository.BedRepository;
import com.example.hostelManagementTool.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BedService {

    @Autowired
    BedRepository bedRepository;
    @Autowired
    RoomRepository roomRepository;

    public Bed addBed(Bed bed){
        return bedRepository.save(bed);
    }

}
