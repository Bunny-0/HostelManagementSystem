package com.example.hostelManagementTool.Repository;

import com.example.hostelManagementTool.Model.Hostel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface HostelRepository extends JpaRepository<Hostel, Integer> {

    public Hostel findByType(String type);
}
