package com.example.hostelManagementTool.Services;
import com.example.hostelManagementTool.Model.Bed;
import com.example.hostelManagementTool.Model.Hostel;
import com.example.hostelManagementTool.Model.Room;
import com.example.hostelManagementTool.Repository.BedRepository;
import com.example.hostelManagementTool.Repository.HostelRepository;
import com.example.hostelManagementTool.Repository.RoomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
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


        @Transactional
        @KafkaListener(topics = {"Allot_Hostel"},groupId = "friends_group")
        public  void allotHostel(String data) throws Exception {
                JSONObject message=objectMapper.readValue(data,JSONObject.class);
                int regNumber= (int) message.get("RollNo");
                String hostelType= (String) message.get("HostelType");
                String roomType= (String) message.get("RoomType");
                Hostel hostel=hostelRepository.findByType(hostelType);
                List<Room> roomList=hostel.getRoomList();
                List<Room>filteredRoom=roomList.stream().filter(room -> room.isAvailable() && room.getRoomType().equals(roomType)).collect(Collectors.toList());
                if(filteredRoom.size()==0){
                        throw new Exception("Room Not Available");
                }
//                List<Bed> bookingBed=null;
                Map<Integer,List<Integer>> bookingRoom=new TreeMap<>();
                for(Room room:filteredRoom){
                        if(!room.getBedList().isEmpty()) {
                                for (Bed bed : room.getBedList()) {
                                        if (bed.isAvailable()) {
                                                if (bookingRoom.containsKey(room.getRoomNo())) {
                                                        int roomNo = room.getRoomNo();
                                                        List<Integer> bedList = bookingRoom.get(roomNo);
                                                        bedList.add(bed.getBedNo());
                                                        bookingRoom.put(roomNo, bedList);

                                                } else {
                                                        List<Integer> bedList = new ArrayList<>();
                                                        bedList.add(bed.getBedNo());
                                                        bookingRoom.put(room.getRoomNo(), bedList);
                                                }

                                        }
                                }
                        }
                }
                System.out.println("Here is the data sent"+bookingRoom+" and "+convertMapToString(bookingRoom));
                kafkaTemplate.send("availableBeds",convertMapToString(bookingRoom));



        }
//        public void bookingData(int roomNo,int bedNo,int regNo,String fromAccount) {
//                JSONObject data = new JSONObject();
//                Bed bed = bedRepository.findById(bedNo).get();
//                data.put("bedNo", bedNo);
//                data.put("regNo", regNo);
//                data.put("fromAccount", fromAccount);
//                data.put("amount", bed.getAmount());
//                data.put("roomNo",roomNo);
//                String message = data.toString();
//                kafkaTemplate.send("create_Transaction", message);
//        }

        @KafkaListener(topics = {"bookBed"},groupId = "friends_group")

        public String bookBed(String message) throws JsonProcessingException {
                JSONObject receivedData=objectMapper.readValue(message,JSONObject.class);
                int bedNo= (int) receivedData.get("bedNo");
                int regNo= (int) receivedData.get("regNo");
                int roomNo=(int) receivedData.get("roomNo");
                Bed bed=bedRepository.findById(bedNo).get();
                bed.setAvailable(false);
                bed.setRegNo(regNo);
                bedRepository.save(bed);
                Room room=roomRepository.findById(roomNo).get();
                List<Bed> bedList=new ArrayList<>();
                for(Bed bed1:room.getBedList()){
                        if(bed1.isAvailable()){
                                bedList.add(bed1);
                        }
                }

//                List<Bed> bedList=room.getBedList().stream().filter(bed1 -> bed1.isAvailable()).collect(Collectors.toList());
                if(bedList.isEmpty()){
                        room.setAvailable(false);
                }
                roomRepository.save(room);
                System.out.println("Booked");
                return "Booked";
        }


        public Hostel addHostel(Hostel hostel){
                return hostelRepository.save(hostel);
        }

        public String convertMapToString(Map<Integer, List<Integer>> map) throws JsonProcessingException {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString(map);
        }
}
