package com.example.demo.service;
import com.example.demo.dto.CommentDto;
import com.example.demo.dto.CommentRequest;
import com.example.demo.feign.LikeFeign;
import com.example.demo.feign.UserFeign;
import com.example.demo.model.Comment;
import com.example.demo.repo.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    CommentRepo commentRepo;
    @Autowired
    LikeFeign likeFeign;
    @Autowired
    UserFeign userFeign;
    @Override
    public List<Comment> getComments(String postId) {
        return commentRepo.findByPostId(postId);
    }
    @Override
    public CommentDto createComment(String postId, CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setComment(commentRequest.getComment());
        comment.setCommentedBy(commentRequest.getCommentedBy());
        comment.setCreatedAt(LocalDate.now());
        comment.setUpdatedAt(LocalDate.now());
        commentRepo.save(comment);
        return new CommentDto(comment.getId(),comment.getComment(),comment.getCommentedBy(),likeFeign.getLikesCount(comment.getId()),comment.getCreatedAt(),comment.getUpdatedAt(),comment.getPostId());
    }
}
