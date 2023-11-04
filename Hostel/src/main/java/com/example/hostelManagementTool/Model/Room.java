package com.example.hostelManagementTool.Model;

import com.example.hostelManagementTool.Enum.RoomType;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Room {

    @Id
    int roomNo;
    boolean available;
    RoomType roomType;


}
