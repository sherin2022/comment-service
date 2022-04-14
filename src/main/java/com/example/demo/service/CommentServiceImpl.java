package com.example.demo.service;

import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.exception.CustomFeignException;
import com.example.demo.feign.LikeFeign;
import com.example.demo.feign.UserFeign;
import com.example.demo.model.Comment;
import com.example.demo.dto.CommentResponse;
import com.example.demo.repo.CommentRepo;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.demo.constant.CommentConstant.*;


@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepo commentRepo;

    @Autowired
    UserFeign userFeign;

    @Autowired
    LikeFeign likeFeign;

    @Override
    public List<CommentResponse> getComments(String postId, Integer page, Integer pageSize) throws CommentNotFoundException {
        try {
            if (page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = 10;
            }
            List<Comment> comments = commentRepo.findByCommentedOnPost(postId, PageRequest.of(page - 1, pageSize));
            List<CommentResponse> commentResponseList = new ArrayList<>();
            for (Comment comment : comments) {
                commentResponseList.add(new CommentResponse(comment.getId(), comment.getComment(), userFeign.getUserDetails(comment.getCommentedBy()), comment.getCommentedOnPost(),
                        likeFeign.getLikesCount(comment.getId()), comment.getCreatedAt(), comment.getUpdatedAt()));
            }
            if (commentResponseList.isEmpty()) {
                throw new CommentNotFoundException(COMMENT_NOT_FOUND + postId);
            }
            return commentResponseList;
        }
        catch (FeignException | HystrixRuntimeException e){
            throw new CustomFeignException(FEIGN_EXCEPTION);
        }
    }



    @Override
    public CommentResponse createComment(String postId, CommentRequest commentRequest) {
        try {
            Comment comment = new Comment();
            comment.setComment(commentRequest.getComment());
            comment.setCommentedBy(commentRequest.getCommentedBy());
            comment.setCreatedAt(new Date());
            comment.setUpdatedAt(new Date());
            comment.setCommentedOnPost(postId);
            Comment newComment = commentRepo.save(new Comment(comment.getId(), comment.getComment(), comment.getCommentedBy(), comment.getCommentedOnPost(), comment.getCreatedAt(), comment.getUpdatedAt()));
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setId(comment.getId());
            UserDto commentedByUser = userFeign.getUserDetails(comment.getCommentedBy());
            commentResponse.setCommentedBy(commentedByUser);
            commentResponse.setCommentedOnPost(postId);
            commentResponse.setLikesCount(0);
            commentResponse.setCreatedAt(comment.getCreatedAt());
            commentResponse.setUpdatedAt(comment.getUpdatedAt());
            commentResponse.setComment(comment.getComment());
            commentResponse.setId(newComment.getId());
            return commentResponse;
        } catch (FeignException | HystrixRuntimeException e) {
            throw new CustomFeignException(FEIGN_EXCEPTION);
        }
    }
   @Override
   public CommentResponse updateComment(String postId, String commentId,CommentRequest commentRequest) {
        try{
            Comment comment = commentRepo.findByCommentedOnPostAndId(postId,commentId);
            if(comment == null){
                throw new CommentNotFoundException(COMMENT_NOT_FOUND + postId + COMMENT_ID + commentId);
            }
            comment.setComment(commentRequest.getComment());
            comment.setUpdatedAt(new Date());
            comment.setComment(commentRequest.getId());
            comment.setCommentedBy(commentRequest.getCommentedBy());
            commentRepo.save(comment);
            return new CommentResponse(comment.getId(),comment.getComment(),userFeign.getUserDetails(comment.getCommentedBy()),
                    comment.getCommentedOnPost(),likeFeign.getLikesCount(comment.getId()),comment.getCreatedAt(),comment.getUpdatedAt());
        }
        catch (FeignException | HystrixRuntimeException | CommentNotFoundException e){
            throw new CustomFeignException(FEIGN_EXCEPTION);
        }
   }

    @Override
    public String deleteComment(String postId, String commentId) throws CommentNotFoundException {
        try{
            commentRepo.deleteById(commentId);
            return COMMENT_DELETED;
        }
        catch (Exception e){
            throw new CommentNotFoundException(COMMENT_NOT_FOUND + postId);
        }
    }

    @Override
    public Integer getCommentsCount(String postId) {
        try {
            List<Comment> comments = commentRepo.findByCommentedOnPost(postId);
            return comments.size();
        }
        catch(Exception e){
            throw new CommentNotFoundException(COMMENT_NOT_FOUND+postId);
        }
    }

    @Override
    public CommentResponse getCommentDetails(String postId, String commentId) {
      try {
          CommentResponse commentResponse = new CommentResponse();
          Comment requiredComment = commentRepo.findByCommentedOnPostAndId(postId,commentId);
          commentResponse.setId(requiredComment.getId());
          commentResponse.setComment(requiredComment.getComment());
          commentResponse.setLikesCount(likeFeign.getLikesCount(commentId));
          commentResponse.setCommentedOnPost(requiredComment.getCommentedOnPost());
          commentResponse.setCreatedAt(requiredComment.getCreatedAt());
          commentResponse.setUpdatedAt(new Date());
          commentResponse.setCommentedBy(userFeign.getUserDetails(requiredComment.getCommentedBy()));
          return commentResponse;
          }catch (FeignException | HystrixRuntimeException e){
              throw new CustomFeignException(FEIGN_EXCEPTION);
          }

    }
}
