package com.example.hostelManagementTool.Model;

import com.example.hostelManagementTool.Enum.RoomType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Data
@Entity
public class Room {

    @Id
    int roomNo;
    boolean available;
    RoomType roomType;
    List<Bed> bedList;


}
