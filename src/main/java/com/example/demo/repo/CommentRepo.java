package com.example.demo.repo;

import com.example.demo.model.Comment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends MongoRepository<Comment,String> {
    List<Comment> findByCommentedOnPost(String postId, PageRequest of);
    Comment findByCommentedOnPostAndId(String postId, String commentId);
    List<Comment> findByCommentedOnPost(String postId);
}
