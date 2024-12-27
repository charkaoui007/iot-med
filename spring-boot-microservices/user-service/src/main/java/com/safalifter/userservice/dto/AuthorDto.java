package com.safalifter.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorDto {
    private String id;
    private String name;
}