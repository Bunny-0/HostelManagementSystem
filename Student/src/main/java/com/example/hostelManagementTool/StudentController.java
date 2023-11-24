package com.example.hostelManagementTool;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    StudentServices studentServices;

    @PostMapping("/addStudent")
    public String  addStudent(@RequestBody Student student){
        return studentServices.AddStudent(student);
    }



}
