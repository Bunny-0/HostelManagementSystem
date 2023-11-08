package com.example.hostelManagementTool;

import javax.persistence.*;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int RegNo;
    String name;
    String userName;
    String Class;
    boolean hostel;

    @Column(columnDefinition = "varchar(255) default 'NA'")
    String HostelType;
    @Column(columnDefinition = "varchar(255) default 'NA'")
    String RoomType;




}
