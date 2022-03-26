package com.example.demo.service;

import com.example.demo.dto.CommentRequest;
import com.example.demo.model.Comment;
import com.example.demo.dto.CommentResponse;

import java.util.List;


public interface CommentService {

    //returns for a particular postId
    public List<Comment> getComments(String postId);
    public CommentResponse createComment(String postId,CommentRequest commentRequest);
    public CommentResponse updateComment(String commentId, CommentRequest commentRequest);
    public String deleteComment(String commentId);
    public Long getCommentsCount(String postId);
    CommentResponse getCommentDetails(String postId,String commentId);
}
