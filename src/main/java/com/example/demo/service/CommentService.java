package com.example.demo.service;



import com.example.demo.dto.CommentDto;
import com.example.demo.dto.CommentRequest;
import com.example.demo.model.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getComments(String postId);
    CommentDto createComment(String postId, CommentRequest commentRequest);

}
