package com.example.demo.Repo;

import com.example.demo.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends MongoRepository<Comment,String> {
    List<Comment> findByCommentedOnPost(String postId);
    Comment findByCommentId(String commentId);
    Comment deleteByCommentId(String commentId);
}
