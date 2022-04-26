package com.example.demo.service;

import com.example.demo.dto.CommentRequest;
import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.model.Comment;
import com.example.demo.dto.CommentResponse;

import java.util.List;


public interface CommentService {

    public List<CommentResponse> getComments(String postId,Integer page,Integer pageSize) throws CommentNotFoundException;
    public CommentResponse createComment(String postId,CommentRequest commentRequest);
    public CommentResponse updateComment(String postId, String commentId,CommentRequest commentRequest);
    public String deleteComment(String postId,String commentId) throws CommentNotFoundException;
    public Integer getCommentsCount(String postId);
    CommentResponse getCommentDetails(String postId,String commentId);

}
