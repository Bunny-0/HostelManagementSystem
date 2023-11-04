package com.example.hostelManagementTool.Services;

import com.example.hostelManagementTool.Enum.HostelType;
import com.example.hostelManagementTool.Enum.RoomType;
import com.example.hostelManagementTool.Model.Hostel;
import com.example.hostelManagementTool.Model.Room;
import com.example.hostelManagementTool.Repository.HostelRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

public class HostelServices {
        @Autowired
        HostelRepository hostelRepository;
        @Autowired
        ObjectMapper objectMapper;


        @KafkaListener(topics = {"Allot_Hostel"},groupId = "Group1")
        public void addHostel(String data) throws JsonProcessingException {
                JSONObject message=objectMapper.readValue(data,JSONObject.class);
                int regNumber= (int) message.get("RollNo");
                HostelType hostelType= (HostelType) message.get("HostelType");
                RoomType roomType= (RoomType) message.get("RoomType");
                Hostel hostel=hostelRepository.findById(hostelType).get();
                List<Room> roomList=hostel.getRoomList();



        }
}
