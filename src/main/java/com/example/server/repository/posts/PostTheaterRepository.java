package com.example.server.repository.posts;

import com.example.server.entity.theater.PostTheater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTheaterRepository extends JpaRepository<PostTheater, Long> {

}
