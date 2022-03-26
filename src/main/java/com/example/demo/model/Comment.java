package com.example.demo.model;

import com.example.demo.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection="comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    private String commentId;
    private String comment;
    private String commentedBy;
    private String commentedOnPost;
    private Date createdAt;
    private Date updatedAt;

}
