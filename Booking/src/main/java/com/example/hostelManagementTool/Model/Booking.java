package com.example.hostelManagementTool.Model;

import com.example.hostelManagementTool.BookingStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Booking {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
int id;
BookingStatus status;
}
