package com.example.hostelManagementTool.Controller;

import com.example.hostelManagementTool.Model.Bed;
import com.example.hostelManagementTool.Services.BedService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bed")
public class BedController {


    @Autowired
    BedService bedService;

    @PostMapping("/addBed")
    public Bed addBed(@RequestBody Bed bed){
        return bedService.addBed(bed);
    }


}
