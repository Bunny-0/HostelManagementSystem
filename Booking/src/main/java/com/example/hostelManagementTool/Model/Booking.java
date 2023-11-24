package com.example.hostelManagementTool.Model;

import com.example.hostelManagementTool.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
int id;
BookingStatus status;
}
