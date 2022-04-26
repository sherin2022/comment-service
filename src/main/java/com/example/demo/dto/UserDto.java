package com.example.demo.dto;

import com.example.demo.enums.BloodGroup;
import com.example.demo.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserDto {
    private String id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private String email;
    private String address;
    private Date dateOfBirth;
    private String employeeId;
    private BloodGroup bloodGroup;
    private Gender gender;

}