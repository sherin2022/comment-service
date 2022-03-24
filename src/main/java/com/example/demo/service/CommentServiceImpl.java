package com.example.demo.service;

import com.example.demo.Repo.CommentRepo;
import com.example.demo.model.Comment;
import com.example.demo.dto.CommentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    CommentRepo commentRepo;

    @Override
    public List<Comment> getComments(String postId) {
        return commentRepo.findByCommentedOnPost(postId);
    }

    @Override
    public CommentResponse createComment(String postId,Comment comment) {

        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setCommentId(comment.getCommentId());
        commentResponse.setCommentedBy(comment.getCommentedBy());
        commentResponse.setLikesCount(0);
        commentResponse.setCreatedAt(comment.getCreatedAt());
        commentResponse.setUpdatedAt(comment.getUpdatedAt());
        commentResponse.setComment(comment.getComment());


        commentRepo.save(new Comment(comment.getCommentId(),comment.getComment(),comment.getCommentedBy(),comment.getCommentedOnPost(),comment.getCreatedAt(),comment.getUpdatedAt()));

        return commentResponse;
    }

    @Override
    public Comment updateComment(String commentId, Comment comment) {
        Comment commentToBeUpdated;
        commentToBeUpdated = commentRepo.findByCommentId(commentId);
        commentToBeUpdated.setComment(comment.getComment());
        return commentRepo.save(commentToBeUpdated);
    }

    @Override
    public Comment deleteComment(String postId, String commentId) {
       return commentRepo.deleteByCommentId(commentId);
    }

    @Override
    public Long getCommentsCount(String postId) {
        List comment = commentRepo.findByCommentedOnPost(postId);
        return comment.stream().count();

    }
}
