package com.example.server.repository.posts;

import com.example.server.entity.User;
import com.example.server.entity.posts.PostFilm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostFilmRepository extends JpaRepository<PostFilm, Long> {
    //List<PostFilm> findAllByUserOrderByCreatedDateDesc(User user);



    //Optional<PostFilm> findById(Long id);
}
