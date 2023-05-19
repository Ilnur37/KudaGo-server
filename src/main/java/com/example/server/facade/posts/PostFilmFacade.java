package com.example.server.facade.posts;

import com.example.server.dto.posts.PostFilmDTO;
import com.example.server.entity.film.PostFilm;
import org.springframework.stereotype.Component;

@Component
public class PostFilmFacade {
    public PostFilmDTO postToPostFilmDTO(PostFilm post) {
        PostFilmDTO postDTO = new PostFilmDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setInfo(post.getInfo());
        postDTO.setShortInfo(post.getShortInfo());
        postDTO.setGenre(post.getGenre());
        postDTO.setCinema(post.getCinema());
        postDTO.setImage(post.getImage());
        postDTO.setBackgroundImg(post.getBackgroundImg());
        postDTO.setLikes(post.getLikes());
        postDTO.setRating(post.getRating());
        postDTO.setUsersLiked(post.getLikedUser());

        return postDTO;
    }
}