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

import static com.example.demo.commentException.CommentException.DELETEDCOMMENT;


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
        return new CommentDto(comment.getId(),comment.getComment(),userFeign.getUserById(comment.getCommentedBy()),
                likeFeign.getLikesCount(comment.getId()),comment.getCreatedAt(),comment.getUpdatedAt());

    }
    @Override
    public CommentDto getCommentDetails(String postId, String commentId) {
        Comment comment = commentRepo.findByPostIdAndId(postId, commentId);
        return new CommentDto(comment.getId(),comment.getComment(),userFeign.getUserById(comment.getCommentedBy()),
                likeFeign.getLikesCount(comment.getId()),comment.getCreatedAt(),comment.getUpdatedAt());

    }

    @Override
    public CommentDto updateComment(String postId, CommentRequest commentRequest,String commentId) {
        Comment comment = commentRepo.findByPostIdAndId(postId,commentId);
        comment.setComment(commentRequest.getComment());
        comment.setUpdatedAt(LocalDate.now());
        commentRepo.save(comment);
        return new CommentDto(comment.getId(),comment.getComment(),userFeign.getUserById(comment.getCommentedBy()),
                likeFeign.getLikesCount(comment.getId()),comment.getCreatedAt(),comment.getUpdatedAt());

    }

    @Override
    public String deleteComment(String postId, String commentId) {
        commentRepo.deleteById(commentId);
        return DELETEDCOMMENT;
    }
    @Override
    public Integer getCommentsCount(String postId) {
        List<Comment> comments = commentRepo.findByPostId(postId);
        return comments.size();
    }

}