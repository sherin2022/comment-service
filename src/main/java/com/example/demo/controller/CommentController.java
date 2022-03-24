package com.example.demo.controller;

import com.example.demo.dto.CommentDto;
import com.example.demo.dto.CommentRequest;
import com.example.demo.model.Comment;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public class CommentController {
    @Autowired
    CommentService commentService;
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable("postId") String postId){
        return new ResponseEntity<>(commentService.getComments(postId), HttpStatus.OK);
    }
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable("postId") String postId, @Valid @RequestBody CommentRequest commentRequest){
        return new ResponseEntity<>(commentService.createComment(postId,commentRequest), HttpStatus.CREATED);
    }
}
