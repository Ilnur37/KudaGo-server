package com.example.server.repository.comments;

import com.example.server.entity.fromKaver.CommentKaver;
import com.example.server.entity.fromKaver.PostKaver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentKaverRepository extends JpaRepository<CommentKaver, Long> {
    List<CommentKaver> findAllByPost(PostKaver post);
}
