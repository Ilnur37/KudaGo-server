package com.example.server.facade;

import com.example.server.dto.PostFilmDTO;
import com.example.server.entity.film.PostFilm;
import org.springframework.stereotype.Component;

@Component
public class PostFilmFacade {
    public PostFilmDTO postToPostFilmDTO(PostFilm postFilm) {
        PostFilmDTO postFilmDTO = new PostFilmDTO();
        postFilmDTO.setId(postFilm.getId());
        postFilmDTO.setTitle(postFilm.getTitle());
        postFilmDTO.setInfo(postFilm.getInfo());
        postFilmDTO.setShortInfo(postFilm.getShortInfo());
        postFilmDTO.setGenre(postFilm.getGenre());
        postFilmDTO.setCinema(postFilm.getCinema());
        postFilmDTO.setImage(postFilm.getImage());
        postFilmDTO.setBackgroundImg(postFilm.getBackgroundImg());
        postFilmDTO.setLikes(postFilm.getLikes());
        postFilmDTO.setUsersLiked(postFilm.getLikedUser());

        return postFilmDTO;
    }
}