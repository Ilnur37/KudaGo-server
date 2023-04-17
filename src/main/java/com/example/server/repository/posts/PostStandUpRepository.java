package com.example.server.repository.posts;

import com.example.server.entity.standUp.PostStandUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostStandUpRepository extends JpaRepository<PostStandUp, Long> {
}
