package com.example.demo.repo;

import com.example.demo.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepo extends MongoRepository<Comment,String> {
    List<Comment> findByPostId(String postId);


    Comment findByPostIdAndId(String postId, String commentId);

    List<Comment> findByPostId(String postId, Pageable page);
}