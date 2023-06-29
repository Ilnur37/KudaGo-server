package com.example.server.repository.comments;

import com.example.server.entity.tags.CommentTag;
import com.example.server.entity.tags.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentTagRepository extends JpaRepository<CommentTag, Long> {
    List<CommentTag> findAllByPost(PostTag post);
}
