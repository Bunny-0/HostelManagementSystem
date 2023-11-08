package com.example.hostelManagementTool.Model;

import lombok.Builder;
import lombok.Data;
import org.springframework.transaction.TransactionStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Builder
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    private  String transactionId= UUID.randomUUID().toString();
    private int amount;
    private String toUser;
    private String fromUser;
    //    private TransactionStatus transactionStatus;
    private String transactionStatus;
    private String transactionTime;
}
