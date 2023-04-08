package com.example.server.repository.images;

import com.example.server.entity.images.ImageFilmModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageFilmRepository extends JpaRepository<ImageFilmModel, Long> {
    //Optional<ImageFilmModel> findByUserId(Long userId);

    Optional<ImageFilmModel> findByPostId(Long postId);
}
