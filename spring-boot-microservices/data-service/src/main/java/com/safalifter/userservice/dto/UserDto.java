package com.safalifter.medicaldataservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.safalifter.userservice.enums.Role;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String id;
    private String username;
    private String email;
    private Role role; // Make sure this line exists
}