package com.example.server.repository.comments;

import com.example.server.entity.film.CommentFilm;
import com.example.server.entity.film.PostFilm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentFilmRepository extends JpaRepository<CommentFilm, Long> {
    List<CommentFilm> findAllByPost(PostFilm post);

    CommentFilm findByIdAndUserId(Long commentId, Long userId);
}
