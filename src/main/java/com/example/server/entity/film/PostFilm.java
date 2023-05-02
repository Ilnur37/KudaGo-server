package com.example.server.entity.film;

import javax.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class PostFilm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String info;
    private String shortInfo;
    private String genre;
    private String cinema;
    private String image;
    private String backgroundImg;
    private Integer likes;

    @Column
    @ElementCollection(targetClass = String.class)
    private Set<String> likedUser = new HashSet<>();

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER,
    mappedBy = "post", orphanRemoval = true)
    private List<CommentFilm> comments = new ArrayList<>();

    public PostFilm() {
    }
}
