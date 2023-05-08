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
    @Column(length = 5000)
    private String info;
    @Column(length = 3000)
    private String shortInfo;
    private String genre;
    private String cinema;
    private String image;
    private String backgroundImg;
    private Integer likes;
    private String rating;
    private String detailsLink;

    @Column
    @ElementCollection(targetClass = String.class)
    private Set<String> likedUser = new HashSet<>();

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER,
    mappedBy = "post", orphanRemoval = true)
    private List<CommentFilm> comments = new ArrayList<>();

    public PostFilm() {
    }
}
