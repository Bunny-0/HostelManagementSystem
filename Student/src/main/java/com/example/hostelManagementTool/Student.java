package com.example.hostelManagementTool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int RegNo;
    String name;
    String userName;
    String branch;
    boolean hostel;

    @Column(columnDefinition = "varchar(255) default 'NA'")
    String HostelType;
    @Column(columnDefinition = "varchar(255) default 'NA'")
    String RoomType;




}
