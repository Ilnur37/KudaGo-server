package com.example.server.entity.comments;

import com.example.server.entity.posts.PostFilm;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class CommentFilm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private PostFilm post;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private Long userId;
    @Column(columnDefinition = "text", nullable = false)
    private String message;
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

}
