package com.example.demo.exception;

public class CommentIdMismatchException extends RuntimeException{
    public CommentIdMismatchException(String s){
        super(s);
    }
}