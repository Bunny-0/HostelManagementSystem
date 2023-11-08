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
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
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
        public  Map<Integer, List<Integer>> allotHostel(String data) throws Exception {
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
                List<Bed> bookingBed=null;
                Map<Integer,List<Integer>> bookingRoom=new TreeMap<>();
                for(Room room:filteredRoom){
                        for(Bed bed:room.getBedList()){
                                if(bed.isAvailable()){
                                        if(bookingRoom.containsKey(room.getRoomNo()))
                                        {
                                                int roomNo=room.getRoomNo();
                                                List<Integer> bedList=bookingRoom.get(roomNo);
                                                bedList.add(bed.getBedNo());
                                                bookingRoom.put(roomNo,bedList);

                                        }
                                        else {
                                                List<Integer> bedList=null;
                                                bedList.add(bed.getBedNo());
                                                bookingRoom.put(room.getRoomNo(),bedList);
                                        }

                                }
                        }
                }
                return bookingRoom;


        }
        public void bookingData(int roomNo,int bedNo,int regNo,String fromAccount) {
                JSONObject data = new JSONObject();
                Bed bed = bedRepository.findById(bedNo).get();
                data.put("bedNo", bedNo);
                data.put("regNo", regNo);
                data.put("fromAccount", fromAccount);
                data.put("amount", bed.getAmount());
                data.put("roomNo",roomNo);
                String message = data.toString();
                kafkaTemplate.send("create_Transaction", message);
        }

        @KafkaListener(topics = {"bookBed"},groupId = "group")

        public String bookBed(String message){
                JSONObject jsonObject=new JSONObject();
                int bedNo= (int) jsonObject.get("bedNo");
                int regNo= (int) jsonObject.get("regNo");
                int roomNo=(int) jsonObject.get("roomNo");
                Bed bed=bedRepository.findById(bedNo).get();
                bed.setAvailable(false);
                Room room=roomRepository.findById(roomNo).get();
                List<Bed> bedList=room.getBedList().stream().filter(bed1 -> bed1.isAvailable()).collect(Collectors.toList());
                if(bedList.isEmpty()){
                        room.setAvailable(false);
                }
                roomRepository.save(room);
                return "Booked";
        }


        public Hostel addHostel(Hostel hostel){
                return hostelRepository.save(hostel);
        }
}
