package com.example.server.entity.standUp;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class PostStandUp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String titleInfo;
    @Column(length = 5000)
    private String info;
    @Column(length = 1000)
    private String shortInfo;
    private String genre;
    private String executor;
    private String address;
    private String metro;
    private String image;
    private String mainImage;
    private Integer likes;
    private String detailsLink;

    @Column
    @ElementCollection(targetClass = String.class)
    private Set<String> likedUser = new HashSet<>();

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER,
            mappedBy = "post", orphanRemoval = true)
    private List<CommentStandUp> comments = new ArrayList<>();

    public PostStandUp() {
    }
}
