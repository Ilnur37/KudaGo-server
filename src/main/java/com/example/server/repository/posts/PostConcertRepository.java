package com.example.server.repository.posts;

import com.example.server.entity.concert.PostConcert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostConcertRepository extends JpaRepository<PostConcert, Long> {
}
