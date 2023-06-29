package com.example.server.repository.posts;

import com.example.server.entity.tags.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    List<PostTag> findPostTagByGenre(String genre);
}
