package com.example.hostelManagementTool.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bed {

    @Id
    int bedNo;
    @Column(columnDefinition = "integer default 0")
    int regNo;
    boolean available;
    double amount;
    @ManyToOne
    @JoinColumn
    Room room;

}
