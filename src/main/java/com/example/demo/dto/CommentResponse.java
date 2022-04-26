package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {


    private String id;
    private String comment;
    private UserDto commentedBy;
    private String commentedOnPost;
    private Integer likesCount;
    private Date createdAt;
    private Date updatedAt;

}
