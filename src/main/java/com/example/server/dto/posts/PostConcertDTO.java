package com.example.server.dto.posts;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class PostConcertDTO {
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String info;
    @NotEmpty
    private String shortInfo;
    private String genre;
    private String address;
    private String image;
    private String executor;
    private Integer likes;
    private Set<String> usersLiked;
}
