package com.example.hostelManagementTool.Controller;

import com.example.hostelManagementTool.Model.Hostel;
import com.example.hostelManagementTool.Services.HostelServices;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hostel")
public class HostelController {

    @Autowired
    HostelServices hostelServices;


    @PostMapping("/addHostel")
    public Hostel addHostel(@RequestBody Hostel hostel){
        return hostelServices.addHostel(hostel);
    }

}
