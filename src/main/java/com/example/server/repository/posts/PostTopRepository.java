package com.example.server.repository.posts;

import com.example.server.entity.top10.PostTop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTopRepository extends JpaRepository<PostTop, Long> {
}
