package com.example.server.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CommentStandUpDTO {
    private Long id;
    @NotEmpty
    private String message;
    private String username;
}
