package com.example.hostelManagementTool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;



    public Wallet addBalance(String userName,int amount){
        Wallet wallet= walletRepository.findByUserName(userName);
        wallet.setBalance(wallet.getBalance()+amount);
        walletRepository.save(wallet);
        return wallet;

    }

    public Wallet createWallet(Wallet wallet){
        walletRepository.save(wallet);
        return wallet;
    }

    @KafkaListener(topics = {"updateWallet"},groupId = "friends_group")
    public void updateWallet(String message) throws JsonProcessingException {
        System.out.println("Reached in wallet line number ");
        JSONObject receivedData=objectMapper.readValue(message,JSONObject.class);
        String toAccount= (String) receivedData.get("toAccount");
        String fromAccount= (String) receivedData.get("fromAccount");
        int regNo= (int) receivedData.get("regNo");
        int bedNo= (int) receivedData.get("bedNo");
        int amount=(int) receivedData.get("amount");
        int roomNo=(int) receivedData.get("roomNo");
        String transactionId=(String)receivedData.get("transactionId");
        System.out.println("Reached in wallet line number ");
        Wallet toWallet=walletRepository.findByUserName(toAccount);
        Wallet fromWallet=walletRepository.findByUserName(fromAccount);

        System.out.println("Reached in wallet");
        if(fromWallet.getBalance()>=amount){
            System.out.println("inside balance wallet");
            fromWallet.setBalance(fromWallet.getBalance()-amount);
            toWallet.setBalance(toWallet.getBalance()+amount);
            walletRepository.save(fromWallet);
            walletRepository.save(toWallet);

            JSONObject sendToTransaction =new JSONObject();
            sendToTransaction.put("transactionId",transactionId);
            sendToTransaction.put("transactionStatus","SUCCESS");
            sendToTransaction.put("roomNo",roomNo);
            sendToTransaction.put("bedNo",bedNo);
            sendToTransaction.put("regNo",regNo);


            String sendMessage=sendToTransaction.toString();
            System.out.println("Reached in wallet"+sendMessage);
            kafkaTemplate.send("update_transaction",sendMessage);

        } else {

            JSONObject sendToTransaction =new JSONObject();
            sendToTransaction.put("transactionId",transactionId);
            sendToTransaction.put("transactionStatus","FAILED");
            String sendMessage=sendToTransaction.toString();
            kafkaTemplate.send("update_transaction",sendMessage);

        }



    }

}
