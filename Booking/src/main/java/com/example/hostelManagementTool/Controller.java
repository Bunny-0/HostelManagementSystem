package com.example.hostelManagementTool;

import com.example.hostelManagementTool.Model.BookingData;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("booking")
@RestController
public class Controller {

    @Autowired
    BookingService bookingService;

    @PostMapping("/bookBed")
    public String bookBed(@RequestBody BookingData bookingData) throws JsonProcessingException {
        bookingService.bookBed(bookingData);
        return "Done";
    }

}
