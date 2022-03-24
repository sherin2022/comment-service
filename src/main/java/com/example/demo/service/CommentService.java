package com.example.demo.service;

import com.example.demo.model.Comment;
import com.example.demo.dto.CommentResponse;

import java.util.List;


public interface CommentService {

    //returns for a particular postId
    public List<Comment> getComments(String postId);
    public CommentResponse createComment(String postId,Comment comment);
    public Comment updateComment(String commentId, Comment comment);
    public Comment deleteComment(String postId,String commentId);
    public Long getCommentsCount(String postId);



}
