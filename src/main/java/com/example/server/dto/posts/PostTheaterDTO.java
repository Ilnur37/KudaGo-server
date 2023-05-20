package com.example.server.dto.posts;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class PostTheaterDTO {
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String info;
    @NotEmpty
    private String shortInfo;
    private String genre;
    private String image;
    private Integer likes;
    private String address;
    private String rating;
    private Set<String> usersLiked;
}
