package com.example.demo.controller;

import com.example.demo.dto.CommentRequest;
import com.example.demo.model.Comment;
import com.example.demo.dto.CommentResponse;
import com.example.demo.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/posts")
public class CommentController{

    @Autowired
    CommentService commentService;

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable("postId") String postId){
        return new ResponseEntity<>(commentService.getComments(postId), HttpStatus.OK);
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> getComments(@PathVariable("postId") String postId,@PathVariable("commentId") String commentId){
        return new ResponseEntity<CommentResponse>(commentService.getCommentDetails(postId,commentId), HttpStatus.OK);
    }


    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(@PathVariable("postId") String postId,@RequestBody CommentRequest commentRequest){
        return new ResponseEntity<>(commentService.createComment(postId,commentRequest), HttpStatus.CREATED);
    }

    @GetMapping("{postId}/comments/count")
    public ResponseEntity<Integer> getCommentsCount(@PathVariable("postId") String postId){
        return new ResponseEntity<>(commentService.getCommentsCount(postId), HttpStatus.OK);
    }

    @PutMapping("{postId}/comments/{commentId}")
    public CommentResponse updateComment(@PathVariable("commentId") String commentId, @RequestBody CommentRequest commentRequest){
        return commentService.updateComment(commentId,commentRequest);
    }

    @DeleteMapping("{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") String commentId){
        return new ResponseEntity<String>(commentService.deleteComment(commentId), HttpStatus.OK);
    }


}

