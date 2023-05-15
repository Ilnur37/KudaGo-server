package com.example.server.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class PostStandUpDTO {
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String info;
    @NotEmpty
    private String shortInfo;
    private String genre;
    private String executor;
    private String address;
    private String image;
    private Integer likes;
    private Set<String> usersLiked;
}
