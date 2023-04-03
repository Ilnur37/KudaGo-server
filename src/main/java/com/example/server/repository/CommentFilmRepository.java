package com.example.server.repository;

import com.example.server.entity.comments.CommentFilm;
import com.example.server.entity.posts.PostFilm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentFilmRepository extends JpaRepository<CommentFilm, Long> {
    List<CommentFilm> findAllByPost(PostFilm post);

    CommentFilm findByIdAndUserId(Long commentId, Long userId);
}
