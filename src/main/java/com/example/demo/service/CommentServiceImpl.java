package com.example.demo.service;

import com.example.demo.Repo.CommentRepo;
import com.example.demo.dto.CommentRequest;
import com.example.demo.feign.LikeFeign;
import com.example.demo.feign.UserFeign;
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

    @Autowired
    UserFeign userFeign;

    @Autowired
    LikeFeign likeFeign;

    @Override
    public List<Comment> getComments(String postId) {
        return commentRepo.findByCommentedOnPost(postId);
    }

    @Override
    public CommentResponse createComment(String postId,CommentRequest commentRequest) {

        Comment comment = new Comment();
        comment.setComment(commentRequest.getComment());
        comment.setCommentedBy(commentRequest.getCommentedBy());
        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());
        comment.setCommentedOnPost(postId);
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setCommentId(comment.getCommentId());
        commentResponse.setCommentedBy(userFeign.getUserDetails(comment.getCommentedBy()));
        commentResponse.setCommentedOnPost(postId);
        commentResponse.setLikesCount(0);
        commentResponse.setCreatedAt(comment.getCreatedAt());
        commentResponse.setUpdatedAt(comment.getUpdatedAt());
        commentResponse.setComment(comment.getComment());
        Comment newComment = commentRepo.save(new Comment(comment.getCommentId(),comment.getComment(),comment.getCommentedBy(),comment.getCommentedOnPost(),comment.getCreatedAt(),comment.getUpdatedAt()));
        commentResponse.setCommentId(newComment.getCommentId());
        return commentResponse;
    }

    @Override
    public CommentResponse updateComment(String commentId, CommentRequest commentRequest) {
        Comment commentToBeUpdated;
        CommentResponse commentResponse = new CommentResponse();
        commentToBeUpdated = commentRepo.findByCommentId(commentId);
        commentToBeUpdated.setComment(commentRequest.getComment());
        commentToBeUpdated.setUpdatedAt(new Date());
        commentRepo.save(commentToBeUpdated);
        commentResponse.setCommentId(commentRepo.findByCommentId(commentId).getCommentId());
        commentResponse.setComment(commentRepo.findByCommentId(commentId).getComment());
        commentResponse.setCommentedBy(userFeign.getUserDetails(commentRepo.findByCommentId(commentId).getCommentedBy()));
        commentResponse.setCommentedOnPost(commentRepo.findByCommentId(commentId).getCommentedOnPost());
        commentResponse.setCreatedAt(commentRepo.findByCommentId(commentId).getCreatedAt());
        commentResponse.setUpdatedAt(commentRepo.findByCommentId(commentId).getUpdatedAt());
        commentResponse.setLikesCount(0);
        commentResponse.setCommentedOnPost(commentRepo.findByCommentId(commentId).getCommentedOnPost());
        return commentResponse;
    }

    @Override
    public String deleteComment(String commentId) {
       Comment commentToBeDeleted = commentRepo.deleteByCommentId(commentId);
          return "Deleted the comment successfully";
    }

    @Override
    public Long getCommentsCount(String postId) {
        List comment = commentRepo.findByCommentedOnPost(postId);
        return comment.stream().count();

    }

    @Override
    public CommentResponse getCommentDetails(String postId, String commentId) {

         CommentResponse commentResponse = new CommentResponse();
         Comment requiredComment = commentRepo.findByCommentId(commentId);
         commentResponse.setCommentId(requiredComment.getCommentId());
         commentResponse.setComment(requiredComment.getComment());
         commentResponse.setCommentedBy(userFeign.getUserDetails(requiredComment.getCommentId()));
         return commentResponse;


    }
}
