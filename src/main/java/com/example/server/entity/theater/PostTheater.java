package com.example.server.entity.theater;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class PostTheater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String titleInfo;
    @Column(length = 5000)
    private String info;
    @Column(length = 3000)
    private String shortInfo;
    private String genre;
    private String address;
    private String metro;
    private String rating;
    private String image;
    private String mainImage;
    private Integer likes;
    private String detailsLink;

    @Column
    @ElementCollection(targetClass = String.class)
    private Set<String> likedUser = new HashSet<>();

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER,
            mappedBy = "post", orphanRemoval = true)
    private List<CommentTheater> comments = new ArrayList<>();

    public PostTheater() {
    }
}
