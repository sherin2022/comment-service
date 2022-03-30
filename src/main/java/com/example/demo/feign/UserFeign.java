package com.example.demo.feign;


import com.example.demo.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserFeign {
    @GetMapping("/users/{userId}")
    UserDTO getUserById(@PathVariable("userId") String userId);
}