package com.example.server.repository.posts;

import com.example.server.entity.fromKaver.PostKaver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostKaverRepository extends JpaRepository<PostKaver, Long> {
    List<PostKaver> findPostKaverByGenre(String genre);
}
