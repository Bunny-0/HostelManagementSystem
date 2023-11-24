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
public class Room {

    @Id
    int roomNo;
    boolean available;

    String roomType;
    @ManyToOne
    @JoinColumn
    Hostel hostel;
    @JsonIgnore
    @OneToMany(mappedBy = "room",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    List<Bed> bedList;


}
