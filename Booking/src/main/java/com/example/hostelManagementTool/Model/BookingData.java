package com.example.hostelManagementTool.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BookingData {
    int bedNo;
    @Id
    int regNo;
    String fromAccount;
    int amount;
    int roomNo;

}
