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

    public Wallet createWallet(String userName){
        Wallet wallet=Wallet.builder().userName(userName).balance(1000).build();
        walletRepository.save(wallet);
        return wallet;
    }

    @KafkaListener(topics = {"update_wallet"},groupId = "group")
    public void updateWallet(String message) throws JsonProcessingException {
        JSONObject receivedData=objectMapper.readValue(message,JSONObject.class);
        String toAccount= (String) receivedData.get("toAccount");
        String fromAccount= (String) receivedData.get("fromAccount");
        int regNo= (int) receivedData.get("regNo");
        int bedNo= (int) receivedData.get("bedNo");
        int amount=(int) receivedData.get("amount");
        int roomNo=(int) receivedData.get("roomNo");
        String transactionId=(String)receivedData.get("transactionId");
        Wallet toWallet=walletRepository.findByUserName(toAccount);
        Wallet fromWallet=walletRepository.findByUserName(fromAccount);

        if(fromWallet.getBalance()>=amount){
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
            kafkaTemplate.send("update_transaction",sendMessage);

        }else {

            JSONObject sendToTransaction =new JSONObject();
            sendToTransaction.put("transactionId",transactionId);
            sendToTransaction.put("transactionStatus","FAILED");
            String sendMessage=sendToTransaction.toString();
            kafkaTemplate.send("update_transaction",sendMessage);

        }



    }

}
