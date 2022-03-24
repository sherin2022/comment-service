package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private String commentId;
    private String comment;
    private String commentedBy;  //This the user Id;
    private Integer likesCount;
    private String commentedOnPost; // This is the postid
    private Date createdAt;
    private Date updatedAt;

}
