package com.example.hostelManagementTool.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hostel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String type;
    @JsonIgnore
    @OneToMany(mappedBy = "hostel",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    List<Room> roomList;

}
