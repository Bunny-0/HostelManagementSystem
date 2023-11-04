package com.example.hostelManagementTool;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class StudentServices {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    public Student AddStudent(Student student){
        studentRepository.save(student);

        if(student.hostel){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("RollNo",student.RegNo);
            jsonObject.put("RoomType",student.RoomType);
            jsonObject.put("HostelType",student.HostelType);
            String data=jsonObject.toString();
            kafkaTemplate.send("Allot_Hostel",data);
        }
        return student;
    }
}
