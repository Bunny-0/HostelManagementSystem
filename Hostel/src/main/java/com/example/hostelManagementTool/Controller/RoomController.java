package com.example.hostelManagementTool.Controller;

import com.example.hostelManagementTool.Model.Room;
import com.example.hostelManagementTool.Services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("room")
public class RoomController {

    @Autowired
    RoomService roomService;

    @PostMapping("/addRoom")
    public Room addRoom(@RequestBody Room room){
      return roomService.addRoom(room);
    }

}
