package com.example.server.facade;

import com.example.server.dto.PostStandUpDTO;
import com.example.server.entity.standUp.PostStandUp;
import org.springframework.stereotype.Component;

@Component
public class PostStandUpFacade {
    public PostStandUpDTO postToPostStandUpDTO(PostStandUp postFilm) {
        PostStandUpDTO postFilmDTO = new PostStandUpDTO();
        postFilmDTO.setId(postFilm.getId());
        postFilmDTO.setTitle(postFilm.getTitle());
        postFilmDTO.setInfo(postFilm.getInfo());
        postFilmDTO.setShortInfo(postFilm.getShortInfo());
        postFilmDTO.setGenre(postFilm.getGenre());
        postFilmDTO.setExecutor(postFilm.getExecutor());
        postFilmDTO.setAddress(postFilm.getAddress());
        postFilmDTO.setImage(postFilm.getImage());
        postFilmDTO.setLikes(postFilm.getLikes());
        postFilmDTO.setUsersLiked(postFilm.getLikedUser());

        return postFilmDTO;
    }
}
