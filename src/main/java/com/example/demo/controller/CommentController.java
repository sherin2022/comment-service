package com.example.demo.controller;

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

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(@PathVariable("postId") String postId,@RequestBody Comment comment){
        return new ResponseEntity<>(commentService.createComment(postId,comment), HttpStatus.CREATED);
    }

    @GetMapping("{postId}/comments/count")
    public ResponseEntity<Long> getCommentsCount(@PathVariable("postId") String postId){
        return new ResponseEntity<>(commentService.getCommentsCount(postId), HttpStatus.OK);
    }

}

