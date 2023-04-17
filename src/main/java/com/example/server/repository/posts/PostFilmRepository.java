package com.example.server.repository.posts;

import com.example.server.entity.film.PostFilm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostFilmRepository extends JpaRepository<PostFilm, Long> {
    //List<PostFilm> findAllByUserOrderByCreatedDateDesc(User user);



    //Optional<PostFilm> findById(Long id);
}
