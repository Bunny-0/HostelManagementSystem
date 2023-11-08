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
import org.springframework.kafka.core.KafkaTemplate;
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
    String toAccount="official";
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

@KafkaListener(topics = {"create_Transaction"},groupId = "Group2")
    public void bookBed(String message) throws JsonProcessingException {
        JSONObject data=objectMapper.readValue(message,JSONObject.class);
        int bedNo= (int) data.get("bedNo");
        int regNo= (int) data.get("regNo");
        int amount=(int) data.get("amount");
        int roomNo=(int) data.get("roomNo");
        String fromAccount= (String) data.get("fromAccount");
        Transaction transaction=Transaction.builder().toUser(toAccount).fromUser(fromAccount).transactionStatus("PENDING").amount(amount).transactionId(UUID.randomUUID().toString()).transactionTime(String.valueOf(new Date())).build();
        transactionRepo.save(transaction);
        JSONObject transData=new JSONObject();
        transData.put("fromAccount" ,fromAccount);
        transData.put("toAccount",toAccount);
        transData.put("regNo",regNo);
        transData.put("bedNo",bedNo);
        transData.put("roomNo",roomNo);
         transData.put("transactionId",transaction.getTransactionId());
        String TransactionData=transData.toString();
        kafkaTemplate.send("updateWallet",TransactionData);

}

@KafkaListener(topics = {"update_transaction"},groupId = "group")
    public void updateTransaction(String message){

    JSONObject receivedData=new JSONObject();
    String transactionId= (String) receivedData.get("transactionId");
    String status= (String) receivedData.get("transactionStatus");
    int bedNo= (int) receivedData.get("bedNo");
    int roomNo= (int) receivedData.get("roomNo");
    int regNo= (int) receivedData.get("regNo");


    Transaction obj=transactionRepo.findByTransactionId(transactionId);

    obj.setTransactionStatus(status);
    transactionRepo.save(obj);
    JSONObject bookData=new JSONObject();
    bookData.put("regNo",regNo);
    bookData.put("roomNo",roomNo);
    bookData.put("bedNo",bedNo);
    String sendBookingData=bookData.toString();
    kafkaTemplate.send("bookBed",sendBookingData);


}

}
