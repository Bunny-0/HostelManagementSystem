package com.example.hostelManagementTool.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Bed {

    @Id
    int bedNo;
    int regNo;
    boolean available;
    double amount;

}
