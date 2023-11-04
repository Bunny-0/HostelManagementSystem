package com.example.hostelManagementTool.Repository;

import com.example.hostelManagementTool.Enum.HostelType;
import com.example.hostelManagementTool.Model.Hostel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostelRepository extends JpaRepository<Hostel, HostelType> {
}
