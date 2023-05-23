package com.example.server.dto.posts;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class PostStandUpDTO {
    private Long id;
    @NotEmpty
    private String title;
    private String titleInfo;
    private String info;
    private String shortInfo;
    private String genre;
    private String executor;
    private String address;
    private String metro;
    private String image;
    private String mainImage;
    private Integer likes;
    private Set<String> usersLiked;
}
