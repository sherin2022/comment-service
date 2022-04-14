package com.example.demo.service;

import com.example.demo.constant.CommentConstant;
import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.exception.CustomFeignException;
import com.example.demo.feign.LikeFeign;
import com.example.demo.feign.UserFeign;
import com.example.demo.model.Comment;
import com.example.demo.repo.CommentRepo;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.demo.constant.CommentConstant.COMMENT_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CommentServiceTest {

    @Autowired
    CommentServiceImpl commentService;

    @MockBean
    CommentRepo commentRepo;

    @MockBean
    UserFeign userFeign;

    @MockBean
    private LikeFeign likeFeign;

    @Test
    void testGetComments() throws CommentNotFoundException {
        when(this.userFeign.getUserDetails((String) any())).thenReturn(new UserDto());
        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);

        Comment comment = new Comment();
        comment.setComment("Method getComment Test");
        comment.setCommentedBy("12");
        comment.setCreatedAt(new Date());
        comment.setId("1");
        comment.setCommentedOnPost("1");
        comment.setUpdatedAt(new Date());

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        when(this.commentRepo.findByCommentedOnPost((String) any(), (PageRequest) any())).thenReturn(commentList);
        try {
            assertEquals(1, this.commentService.getComments("1", 1, 3).size());
        } catch (Exception e) {
            throw new CommentNotFoundException(COMMENT_NOT_FOUND);

        }
        verify(this.userFeign).getUserDetails((String) any());
        verify(this.likeFeign).getLikesCount((String) any());
        verify(this.commentRepo).findByCommentedOnPost((String) any(), (PageRequest) any());

    }

    @Test
    void testExceptionThrownWhenFeignConnectionIssueForGetAllComments() {
        when(this.userFeign.getUserDetails((String) any())).thenReturn(new UserDto());
        when(this.likeFeign.getLikesCount((String) any())).thenThrow(mock(FeignException.class));

        Comment comment = new Comment();
        comment.setComment("Comment Test");
        comment.setCommentedBy("test user");
        comment.setCreatedAt(new Date());
        comment.setId("1");
        comment.setCommentedOnPost("1");
        comment.setUpdatedAt(new Date());

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        when(this.commentRepo.findByCommentedOnPost((String) any(), (PageRequest) any())).thenReturn(commentList);
        assertThrows(CustomFeignException.class, () -> this.commentService.getComments("1", 1, 3));
        verify(this.userFeign).getUserDetails((String) any());
        verify(this.likeFeign).getLikesCount((String) any());
        verify(this.commentRepo).findByCommentedOnPost((String) any(), (PageRequest) any());
    }

    @Test
    void testGetCommentDetailsById() {
        UserDto userDto = new UserDto();
        when(this.userFeign.getUserDetails((String) any())).thenReturn(userDto);
        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);

        Comment testComment = new Comment();
        testComment.setComment("Comment Test");
        testComment.setCommentedBy("test user");
        testComment.setCreatedAt(new Date());
        testComment.setId("1");
        testComment.setCommentedOnPost("1");
        testComment.setUpdatedAt(new Date());


        when(this.commentRepo.findByCommentedOnPostAndId((String) any(), (String) any())).thenReturn((testComment));
        CommentResponse commentResponse= this.commentService.getCommentDetails("1", "1");
        assertEquals("Comment Test", commentResponse.getComment());
        assertEquals(3, commentResponse.getLikesCount());
        assertNotNull(commentResponse.getId());
        assertSame(userDto, commentResponse.getCommentedBy());
        verify(this.userFeign).getUserDetails((String) any());
        verify(this.likeFeign).getLikesCount((String) any());

    }



    @Test
        void testCreateComment() {
            UserDto userDto = new UserDto();
            when(this.userFeign.getUserDetails((String) any())).thenReturn(userDto);
            Comment comment = new Comment();
            comment.setComment("Test method createComment");
            comment.setCommentedBy("test user");
            comment.setCreatedAt(new Date());
            comment.setId("1");
            comment.setCommentedOnPost("1");
            comment.setUpdatedAt(new Date());
            when(this.commentRepo.save((Comment) any())).thenReturn(comment);
            CommentResponse commentResponse = this.commentService.createComment("1", new CommentRequest("1", "Test method createComment", "test user"));
            assertEquals("Test method createComment", commentResponse.getComment());
            assertEquals(0, commentResponse.getLikesCount());
            assertNotNull(commentResponse.getId());
            assertSame(userDto, commentResponse.getCommentedBy());
            verify(this.userFeign).getUserDetails((String) any());
            verify(this.commentRepo).save((Comment) any());
        }

    @Test
    void testExceptionThrownWhenFeignConnectionIssueForCreateComment() {
        CommentRequest commentRequest = new CommentRequest("1", "Comment test", "test user");
        when(this.userFeign.getUserDetails((String) any())).thenReturn(new UserDto());
        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);
        when(this.commentRepo.save((Comment) any())).thenThrow(mock(FeignException.class));
        assertThrows(CustomFeignException.class, () -> this.commentService.createComment("1", commentRequest));
        verify(this.commentRepo).save((Comment) any());
    }
    @Test
    void testGetCommentsCountById() {
        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.setId("1");
        comment1.setComment("comment test 1");
        Comment comment2 = new Comment();
        comment2.setId("2");
        comment2.setComment("comment test 2");
        comments.add(comment1);
        comments.add(comment2);

        Mockito.when(commentRepo.findByCommentedOnPost("1")).thenReturn(comments);
        assertThat(commentService.getCommentsCount("1"));
    }
    @Test
    void testExceptionThrownWhenGetCommentsCountNotFound() {
        Mockito.doThrow(CommentNotFoundException.class).when(commentRepo).findByCommentedOnPost(any());
        Exception commentNotFoundException = assertThrows(CommentNotFoundException.class, () -> commentService.getCommentsCount("1"));
        assertTrue(commentNotFoundException.getMessage().contains(CommentConstant.COMMENT_NOT_FOUND));
    }


    @Test
    void testExceptionThrownWhenCommentNotFoundById() {
        when(this.commentRepo.findByCommentedOnPost((String) any(), (PageRequest) any())).thenReturn(new ArrayList<>());
        assertThrows(CommentNotFoundException.class, () -> this.commentService.getComments("1", 1, 3));
        verify(this.commentRepo).findByCommentedOnPost((String) any(), (PageRequest) any());
    }

    @Test
    void testUpdateCommentById() {
        UserDto userDto = new UserDto();
        when(this.userFeign.getUserDetails((String) any())).thenReturn(userDto);
        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);

        Comment testcomment = new Comment();
        testcomment.setComment("Comment created");
        testcomment.setCommentedBy("test user");
        testcomment.setCreatedAt(new Date());
        testcomment.setId("1");
        testcomment.setCommentedOnPost("1");
        testcomment.setUpdatedAt(new Date());

        Comment testupdatedcomment = new Comment();
        testupdatedcomment.setComment("Comment updated");
        testupdatedcomment.setCommentedBy("test user");
        testupdatedcomment.setCreatedAt(new Date());
        testupdatedcomment.setId("1");
        testupdatedcomment.setCommentedOnPost("1");
        testupdatedcomment.setUpdatedAt(new Date());

        when(this.commentRepo.save((Comment) any())).thenReturn(testupdatedcomment);
        when(this.commentRepo.findByCommentedOnPostAndId((String) any(), (String) any())).thenReturn((testcomment));
        CommentResponse updatedCommentResult = this.commentService.updateComment("1",  "1",new CommentRequest("1", "Comment updated", "test user"));
        assertEquals("Comment updated", updatedCommentResult.getComment());
        assertEquals(3, updatedCommentResult.getLikesCount());
        assertEquals("1", updatedCommentResult.getId());
        assertEquals((new Date()).toString(), updatedCommentResult.getCreatedAt().toString());
        assertSame(userDto, updatedCommentResult.getCommentedBy());
        verify(this.userFeign).getUserDetails((String) any());
        verify(this.likeFeign).getLikesCount((String) any());
        verify(this.commentRepo).findByCommentedOnPostAndId((String) any(), (String) any());
        verify(this.commentRepo).save((Comment) any());
    }


    @Test
    void testExceptionThrownWhenFeignConnectionIssueForUpdateCommentById() {
        when(this.userFeign.getUserDetails((String) any())).thenReturn(new UserDto());
        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);

        CommentRequest commentRequest = new CommentRequest("1", "Comment", "Commented By");
        Comment comment = new Comment();
        comment.setComment("Comment Test");
        comment.setCommentedBy("test user");
        comment.setCreatedAt(new Date());
        comment.setId("1");
        comment.setCommentedOnPost("1");
        comment.setUpdatedAt(new Date());
        when(this.commentRepo.save((Comment) any())).thenThrow(mock(FeignException.class));
        when(this.commentRepo.findByCommentedOnPostAndId((String) any(), (String) any())).thenReturn(comment);
        assertThrows(CustomFeignException.class, () -> this.commentService.updateComment("1","1",commentRequest));
        verify(this.commentRepo).findByCommentedOnPostAndId((String) any(), (String) any());
        verify(this.commentRepo).save((Comment) any());
    }
    @Test
    void testDeleteCommentById() throws CommentNotFoundException {
        commentService.deleteComment("1", "2");
        verify(commentRepo, times(1)).deleteById("2");
    }



    @Test
    void testExceptionThrownForCommentNotFoundWhenDeleteCommentById() {
        Mockito.doThrow(CommentNotFoundException.class).when(commentRepo).deleteById(any());
        Exception userNotFoundException = assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment("1", "2"));
        assertTrue(userNotFoundException.getMessage().contains(CommentConstant.COMMENT_NOT_FOUND));
    }

    }


