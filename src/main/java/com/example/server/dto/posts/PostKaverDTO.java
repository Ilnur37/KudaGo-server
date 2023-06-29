package com.example.server.dto.posts;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class PostKaverDTO {
    private Long id;
    @NotEmpty
    private String title;
    private String info;
    private String genre;
    private String address;
    private String image;
    private String mainImage;
    private Integer likes;
    private Set<String> usersLiked;
    private String referenceInfo;
}
