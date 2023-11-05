package com.example.hostelManagementTool.Repository;

import com.example.hostelManagementTool.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Integer> {
}
