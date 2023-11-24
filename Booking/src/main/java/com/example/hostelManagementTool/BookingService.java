package com.example.hostelManagementTool;

import com.example.hostelManagementTool.Model.Booking;
import com.example.hostelManagementTool.Model.BookingData;
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

    public void bookBed(BookingData bookingData) throws JsonProcessingException {
        Transaction transaction=Transaction.builder().toUser(toAccount).fromUser(bookingData.getFromAccount()).transactionStatus("PENDING").amount(bookingData.getAmount()).transactionId(UUID.randomUUID().toString()).transactionTime(String.valueOf(new Date())).build();
        transactionRepo.save(transaction);
        JSONObject transData=new JSONObject();
        transData.put("fromAccount" ,bookingData.getFromAccount());
        transData.put("toAccount",toAccount);
        transData.put("regNo",bookingData.getRegNo());
        transData.put("bedNo",bookingData.getBedNo());
        transData.put("roomNo",bookingData.getRoomNo());
        transData.put("amount",bookingData.getAmount());
         transData.put("transactionId",transaction.getTransactionId());
        String TransactionData=transData.toString();
        System.out.println("Reached in Booking"+TransactionData);
        kafkaTemplate.send("updateWallet",TransactionData);
}

@KafkaListener(topics = {"update_transaction"},groupId = "group")
    public void updateTransaction(String message) throws JsonProcessingException {
    JSONObject receivedData=objectMapper.readValue(message,JSONObject.class);
    System.out.println("Reached in Transaction");
    String transactionId= (String) receivedData.get("transactionId");
    String status= (String) receivedData.get("transactionStatus");
    int bedNo= (int) receivedData.get("bedNo");
    int roomNo= (int) receivedData.get("roomNo");
    int regNo= (int) receivedData.get("regNo");

    System.out.println("Reached in Transaction"+receivedData);
    Transaction obj=transactionRepo.findByTransactionId(transactionId);

    obj.setTransactionStatus(status);
    transactionRepo.save(obj);
    JSONObject bookData=new JSONObject();
    bookData.put("regNo",regNo);
    bookData.put("roomNo",roomNo);
    bookData.put("bedNo",bedNo);
    String sendBookingData=bookData.toString();
    System.out.println("Reached in Transaction"+sendBookingData);
    kafkaTemplate.send("bookBed",sendBookingData);


}

}
