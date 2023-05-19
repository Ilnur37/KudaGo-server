package com.example.server.repository.comments;

import com.example.server.entity.concert.CommentConcert;
import com.example.server.entity.concert.PostConcert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentConcertRepository extends JpaRepository<CommentConcert, Long> {
    List<CommentConcert> findAllByPost(PostConcert post);
}
