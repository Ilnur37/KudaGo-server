package com.example.server.facade.posts;

import com.example.server.dto.posts.PostTheaterDTO;
import com.example.server.entity.theater.PostTheater;
import org.springframework.stereotype.Component;

@Component
public class PostTheaterFacade {
    public PostTheaterDTO postToPostTheaterDTO(PostTheater post) {
        PostTheaterDTO postDTO = new PostTheaterDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setInfo(post.getInfo());
        postDTO.setShortInfo(post.getShortInfo());
        postDTO.setGenre(post.getGenre());
        postDTO.setImage(post.getImage());
        postDTO.setLikes(post.getLikes());
        postDTO.setAddress(post.getAddress());
        postDTO.setRating(post.getRating());
        postDTO.setUsersLiked(post.getLikedUser());

        return postDTO;
    }
}
