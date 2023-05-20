package com.example.server.repository.comments;

import com.example.server.entity.theater.CommentTheater;
import com.example.server.entity.theater.PostTheater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentTheaterRepository extends JpaRepository<CommentTheater, Long> {
    List<CommentTheater> findAllByPost(PostTheater post);
}
