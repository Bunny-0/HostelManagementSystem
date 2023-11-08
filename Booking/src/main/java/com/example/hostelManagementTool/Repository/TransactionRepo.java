package com.example.hostelManagementTool.Repository;

import com.example.hostelManagementTool.Model.Transaction;
import com.example.hostelManagementTool.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transaction,Integer> {

    public Transaction findByTransactionId(String transactionId);
}
