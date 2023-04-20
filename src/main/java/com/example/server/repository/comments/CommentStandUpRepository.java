package com.example.server.repository.comments;

import com.example.server.entity.standUp.CommentStandUp;
import com.example.server.entity.standUp.PostStandUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentStandUpRepository extends JpaRepository<CommentStandUp, Long> {
    List<CommentStandUp> findAllByPost(PostStandUp post);
}
