package com.example.hostelManagementTool.Services;

import com.example.hostelManagementTool.Model.Bed;
import com.example.hostelManagementTool.Repository.BedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BedService {

    @Autowired
    BedRepository bedRepository;

    public void addBed(Bed bed){
        bedRepository.save(bed);
    }

}
