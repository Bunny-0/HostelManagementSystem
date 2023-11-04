package com.example.hostelManagementTool.Services;

import com.example.hostelManagementTool.Enum.HostelType;
import com.example.hostelManagementTool.Enum.RoomType;
import com.example.hostelManagementTool.Model.Bed;
import com.example.hostelManagementTool.Model.Hostel;
import com.example.hostelManagementTool.Model.Room;
import com.example.hostelManagementTool.Repository.HostelRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class HostelServices {
        @Autowired
        HostelRepository hostelRepository;
        @Autowired
        ObjectMapper objectMapper;
        @Autowired
        KafkaTemplate<String,String> kafkaTemplate;


        @KafkaListener(topics = {"Allot_Hostel"},groupId = "Group1")
        public void allotHostel(String data) throws Exception {
                JSONObject message=objectMapper.readValue(data,JSONObject.class);
                int regNumber= (int) message.get("RollNo");
                HostelType hostelType= (HostelType) message.get("HostelType");
                RoomType roomType= (RoomType) message.get("RoomType");
                Hostel hostel=hostelRepository.findById(hostelType).get();
                List<Room> roomList=hostel.getRoomList();
                List<Room>filteredRoom=roomList.stream().filter(room -> room.isAvailable() && room.getRoomType().equals(roomType)).collect(Collectors.toList());
                if(filteredRoom.size()==0){
                        throw new Exception("Room Not Available");
                }
                Bed bookingBed=null;
                for(Room room:filteredRoom){
                        for(Bed bed:room.getBedList()){
                                if(bed.isAvailable()){
                                        bookingBed=bed;
                                        break;
                                }
                        }
                }
                JSONObject bookingObject=new JSONObject();
                bookingObject.put("bed",bookingBed);
                bookingObject.put("regNum",regNumber);
                String bookingData=bookingObject.toString();
                kafkaTemplate.send("book_bed",bookingData);












        }

//        public Hostel addHostel(Hostel hostel){
//
//        }
}
