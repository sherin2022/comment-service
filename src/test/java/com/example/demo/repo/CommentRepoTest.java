package com.example.demo.repo;

import com.example.demo.model.Comment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@DataMongoTest
public class CommentRepoTest{

    @Autowired
    private CommentRepo commentRepo;

    @BeforeEach
    void initUseCase() {
       Comment comment = createComment();
       commentRepo.save(comment);
    }

    @AfterEach
    public void destroyAll() {
        commentRepo.deleteAll();
    }

    @Test
    void testfindByCommentedOnPost() {
        List<Comment> comments = commentRepo.findByCommentedOnPost("12");
        assertEquals("12",comments.get(0).getCommentedOnPost());
    }

    @Test
    void testFindByCommentedOnPost() {
        List<Comment> comments = commentRepo.findByCommentedOnPost("12",PageRequest.of(0, 1));
        assertEquals("12",comments.get(0).getCommentedOnPost());

    }

    @Test
    void findByCommentedOnPostAndCommentId() {
        Comment comment = commentRepo.findByCommentedOnPostAndId("12","1");
        assertEquals("12",comment.getCommentedOnPost());
        assertEquals("1",comment.getId());

    }

    private Comment createComment() {
        Comment comment =  new Comment();
        comment.setComment("Test Comment Repo");
        comment.setId("1");
        comment.setCommentedOnPost("12");
        comment.setCommentedBy("13");
        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());
        return comment;
    }


}
