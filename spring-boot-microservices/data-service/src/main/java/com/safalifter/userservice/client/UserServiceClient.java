package com.safalifter.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "user-service", url = "${userservice.url}")
public interface UserServiceClient {

    @GetMapping("/v1/user/getAll") // Adjust the path if needed
    List<com.safalifter.medicaldataservice.dto.UserDto> getAllUsers();

    @GetMapping("/v1/user/getAll") // Adjust the path if needed
    List<com.safalifter.medicaldataservice.dto.UserDto> getAllCaregivers();
}