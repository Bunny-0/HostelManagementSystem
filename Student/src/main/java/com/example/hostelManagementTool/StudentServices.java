package com.example.hostelManagementTool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.Map;

@Service
public class StudentServices {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;
    String data;
    private final Object lock = new Object();

    public String AddStudent(Student student){
        studentRepository.save(student);
            if (student.hostel) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("RollNo", student.RegNo);
                jsonObject.put("RoomType", student.RoomType);
                jsonObject.put("HostelType", student.HostelType);
                String data = jsonObject.toString();
                System.out.println("here is the data" + data);
                kafkaTemplate.send("Allot_Hostel", data);
            }
            while(data==null){

            }
        return data;

    }

    @KafkaListener(topics = {"availableBeds"},groupId = "group")
    public void getAvailableBed(String message) throws JsonProcessingException {

        this.data=message;
        System.out.println("inside available"+data);
    }

}



//Student-->AllotHostel-->Return List<availableBed>(postMan)
//BookService--->fromWallet,Regno,Bedno.
