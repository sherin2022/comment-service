package com.example.demo.controller;


import com.example.demo.constant.CommentConstant;
import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.model.Comment;
import com.example.demo.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @MockBean
    CommentService commentService;

    @Autowired
    MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetCommentsByPostId()throws Exception {
        List<CommentResponse> commentResponse = createCommentList();
        try {
            when(commentService.getComments("2", null, null)).thenReturn(commentResponse);
        } catch (CommentNotFoundException e) {
            e.printStackTrace();
        }
        mockMvc.perform(get("/posts/2/comments"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[1].comment", Matchers.is("commentTestTwo")));
    }



    @Test
    void testCreateComment() throws Exception {
        Comment comment = createOneCommentToPost();
        CommentResponse commentResponse = new CommentResponse();
        CommentRequest commentRequest = new CommentRequest();
        when(commentService.createComment("1",commentRequest)).thenReturn(commentResponse);
        mockMvc.perform(post("/posts/1/comments")
                        .content(asJsonString(comment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void testGetCommentsById() throws Exception {
        CommentResponse commentResponse = createOneComment();
        when(commentService.getCommentDetails("1","2")).thenReturn(commentResponse);
        mockMvc.perform(get("/posts/1/comments/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.aMapWithSize(7)))
                .andExpect(jsonPath("$.comment", Matchers.is("CommentTest")));
    }

    @Test
    void testGetCommentsCountById() throws Exception {
        Integer count = createCommentsToCount();
        Mockito.when(commentService.getCommentsCount("1")).thenReturn(count);
        mockMvc.perform(get("/posts/1/comments/count"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateCommentById() throws Exception {
        Comment comment = createOneCommentToUpdate();
        CommentResponse commentDto = new CommentResponse();
        CommentRequest commentRequest = new CommentRequest();
        when(commentService.updateComment("2", "1",commentRequest)).thenReturn(commentDto);
        mockMvc.perform(put("/posts/1/comments/2")
                        .content(asJsonString(comment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteCommentById() throws Exception, CommentNotFoundException {
        Mockito.when(commentService.deleteComment("2","2")).thenReturn(CommentConstant.COMMENT_DELETED);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/posts/1/comments/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


    private List<CommentResponse> createCommentList() {
        List<CommentResponse> commentDto = new ArrayList<>();

        CommentResponse commentDto1 = new CommentResponse();
        commentDto1.setId("1");
        commentDto1.setComment("commentTestOne");
        commentDto1.setCommentedBy(new UserDto("1","Commenter1","A","B","1234567890","commentor1@mail.com","Bangalore", null,"123",null,null));
        commentDto1.setLikesCount(3);
        commentDto1.setCreatedAt(new Date());
        commentDto1.setUpdatedAt(new Date());

        CommentResponse commentDto2 = new CommentResponse();
        commentDto2.setId("2");
        commentDto2.setComment("commentTestTwo");
        commentDto2.setCommentedBy(new UserDto("2","Commenter2","C","D","1234567890","commentor2@mail.com","Bangalore", null,"132",null,null));
        commentDto2.setLikesCount(3);
        commentDto2.setCreatedAt(new Date());
        commentDto2.setUpdatedAt(new Date());

        commentDto.add(commentDto1);
        commentDto.add(commentDto2);

        return commentDto;
    }



    private Comment createOneCommentToPost() {
        Comment commentDto = new Comment();

        commentDto.setId("1");
        commentDto.setComment("Test Create Comment ");
        commentDto.setCommentedBy(String.valueOf(new UserDto("1","Creator","J","S","9090902002","comment@mail.com","Bangalore", null,"123",null,null)));
        return commentDto;
    }

    private CommentResponse createOneComment() {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId("2");
        commentResponse.setComment("CommentTest");
        commentResponse.setCommentedBy(new UserDto("1","Savita","J","Golabavi","1234567890","savita@mail.com","Bangalore",null,"123",null,null));
        return commentResponse;
    }
    private Comment createOneCommentToUpdate() {
        Comment comment = new Comment();
        comment.setId("2");
        comment.setComment("CommentTest");
        comment.setCommentedBy(String.valueOf(new UserDto("1","Sherin","Ann","Joseph","1234567890","sherin@mail.com","Bangalore",null,"1234",null,null)));
        comment.setCreatedAt(null);
        comment.setUpdatedAt(null);
        return comment;
    }

    private Integer createCommentsToCount() {
        List<Comment> comments = new ArrayList<>();

        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        Comment comment3 = new Comment();

        comments.add(comment1);
        comments.add(comment2);
        comments.add(comment3);
        return comments.size();
    }


}
