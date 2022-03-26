package com.example.demo.feign;

import com.example.demo.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name ="user-service")
public interface UserFeign {

    @GetMapping("/users/{userId}")
    public UserDto getUserDetails(@PathVariable("userId") String userId);


}
