package com.example.hostelManagementTool;

import com.example.hostelManagementTool.Model.Booking;
import com.example.hostelManagementTool.Model.Transaction;
import com.example.hostelManagementTool.Repository.BookingRepository;
import com.example.hostelManagementTool.Repository.TransactionRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class BookingService {


@Autowired
BookingRepository bookingRepository;

@Autowired
    TransactionRepo transactionRepo;
    @Autowired
    ObjectMapper objectMapper;
    String toAccount="1234567890987";

@KafkaListener(topics = {"create_Transaction"},groupId = "Group2")
    public void bookBed(String message) throws JsonProcessingException {
        JSONObject data=objectMapper.readValue(message,JSONObject.class);
        int bedNo= (int) data.get("bedNo");
        int regNo= (int) data.get("regNo");
        int amount=(int) data.get("amount");
        String fromAccount= (String) data.get("fromAccount");
        Transaction transaction=Transaction.builder().toUser(toAccount).fromUser(fromAccount).transactionStatus("PENDING").amount(amount).transactionId(UUID.randomUUID().toString()).transactionTime(String.valueOf(new Date())).build();
        transactionRepo.save(transaction);



}

}
