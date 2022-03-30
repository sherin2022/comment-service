package com.example.demo.service;



import com.example.demo.dto.CommentDto;
import com.example.demo.dto.CommentRequest;
import com.example.demo.model.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getComments(String postId);
    CommentDto createComment(String postId, CommentRequest commentRequest);
    CommentDto getCommentDetails(String postId,String commentId);
    CommentDto updateComment(String postId, CommentRequest commentRequest,String commentId);
    String deleteComment(String postId, String commentId);
    Integer getCommentsCount(String postId);

}
