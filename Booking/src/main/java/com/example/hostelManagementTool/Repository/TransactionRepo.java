package com.example.hostelManagementTool.Repository;

import com.example.hostelManagementTool.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transaction,Integer> {
}
