package com.example.hostelManagementTool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookingService {


@Autowired
    BookingRepository bookingRepository;
    @Autowired
    ObjectMapper objectMapper;

@KafkaListener(topics = {"book_bed"},groupId = "Group2")
    public void bookBed(String message) throws JsonProcessingException {
    JSONObject dataObj=objectMapper.readValue(message,JSONObject.class);
    int bedNo= (int) dataObj.get("bed");

}

}
