package com.example.hostelManagementTool.Model;

import com.example.hostelManagementTool.Enum.HostelType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Data
@Entity
public class Hostel {

    @Id
    HostelType type;
    List<Room> roomList;

}
