package com.example.demo.controller;

import com.example.demo.dto.CommentRequest;
import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.model.Comment;
import com.example.demo.dto.CommentResponse;
import com.example.demo.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/posts")
public class CommentController{

    @Autowired
    CommentService commentService;

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable("postId") String postId, @QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize) throws CommentNotFoundException {
        return new ResponseEntity<>(commentService.getComments(postId,page,pageSize), HttpStatus.OK);
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> getCommentDetails(@PathVariable("postId") String postId,@PathVariable("commentId") String commentId){
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
    public CommentResponse updateComment(@PathVariable("postId") String postId,@PathVariable("commentId") String commentId, @RequestBody CommentRequest commentRequest){
        return commentService.updateComment(postId,commentId,commentRequest);
    }

    @DeleteMapping("{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("postId") String postId ,@PathVariable("commentId") String commentId) throws CommentNotFoundException {
        return new ResponseEntity<String>(commentService.deleteComment(postId,commentId), HttpStatus.OK);
    }


}

