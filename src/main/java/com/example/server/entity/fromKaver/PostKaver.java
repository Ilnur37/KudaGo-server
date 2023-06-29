package com.example.server.entity.fromKaver;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class PostKaver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    @Column(length = 5000)
    private String info;
    private String genre;
    private String address;
    private String image;
    private String mainImage;
    private Integer likes;
    private String detailsLink;
    private String referenceInfo;

    @Column
    @ElementCollection(targetClass = String.class)
    private Set<String> likedUser = new HashSet<>();

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER,
            mappedBy = "post", orphanRemoval = true)
    private List<CommentKaver> comments = new ArrayList<>();

    public PostKaver() {
    }
}
