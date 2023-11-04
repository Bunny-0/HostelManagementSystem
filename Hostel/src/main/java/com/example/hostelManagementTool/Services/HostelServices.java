package com.example.hostelManagementTool.Services;

import com.example.hostelManagementTool.Enum.HostelType;
import com.example.hostelManagementTool.Enum.RoomType;
import com.example.hostelManagementTool.Model.Bed;
import com.example.hostelManagementTool.Model.Hostel;
import com.example.hostelManagementTool.Model.Room;
import com.example.hostelManagementTool.Repository.BedRepository;
import com.example.hostelManagementTool.Repository.HostelRepository;
import com.example.hostelManagementTool.Repository.RoomRepository;
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
        BedRepository bedRepository;
        @Autowired
        RoomRepository roomRepository;
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
                Room bookingRoom=null;
                for(Room room:filteredRoom){
                        for(Bed bed:room.getBedList()){
                                if(bed.isAvailable()){
                                        bookingBed=bed;
                                        bookingRoom=room;
                                        break;
                                }
                        }
                }
                JSONObject bookingObject=new JSONObject();
                bookingObject.put("bedNo",bookingBed.getBedNo());
                bookingObject.put("regNum",regNumber);
                String bookingData=bookingObject.toString();
                kafkaTemplate.send("book_bed",bookingData);
                //if successfull
                bookingBed.setAvailable(false);
                bedRepository.save(bookingBed);

                bookingRoom.getBedList().get(bookingBed.getBedNo()).setAvailable(false);
                roomRepository.save(bookingRoom);

                List<Bed> bedList=bookingRoom.getBedList().stream().filter(bed -> bed.isAvailable()).collect(Collectors.toList());
                if(bedList.size()==0){
                        bookingRoom.setAvailable(false);
                        roomRepository.save(bookingRoom);
                }



















        }

//        public Hostel addHostel(Hostel hostel){
//
//        }
}
