package com.example.server.repository.comments;

import com.example.server.entity.top10.CommentTop;
import com.example.server.entity.top10.PostTop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentTopRepository extends JpaRepository<CommentTop, Long> {
    List<CommentTop> findAllByPost(PostTop post);
}
