package com.example.server.entity;

import javax.persistence.*;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

@Data
@Entity
public class ImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageBytes;
    @JsonIgnore
    private long postId;
    @JsonIgnore
    private long userId;
}