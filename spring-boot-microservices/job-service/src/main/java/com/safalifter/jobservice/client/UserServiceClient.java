package com.safalifter.jobservice.client;

import com.safalifter.jobservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "user-service", path = "/v1/user")
public interface UserServiceClient {

    @GetMapping("/caregivers") // Adjust the path if needed
    List<UserDto> getAllUsers();

    @GetMapping("/getAll") // Adjust the path if needed
    List<UserDto> getAllCaregivers();
}