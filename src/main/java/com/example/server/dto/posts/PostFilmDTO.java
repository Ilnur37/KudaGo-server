package com.example.server.dto.posts;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class PostFilmDTO {
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String info;
    @NotEmpty
    private String shortInfo;
    private String genre;
    private String cinema;
    private String image;
    private String mainImage;
    private Integer likes;
    private String rating;
    private Set<String> usersLiked;
    private String referenceInfo;
}
