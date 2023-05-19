package com.example.server.facade.posts;

import com.example.server.dto.posts.PostConcertDTO;
import com.example.server.entity.concert.PostConcert;
import org.springframework.stereotype.Component;

@Component
public class PostConcertFacade {
    public PostConcertDTO postToPostConcertDTO(PostConcert post) {
        PostConcertDTO postDTO = new PostConcertDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setInfo(post.getInfo());
        postDTO.setShortInfo(post.getShortInfo());
        postDTO.setGenre(post.getGenre());
        postDTO.setExecutor(post.getExecutor());
        postDTO.setAddress(post.getAddress());
        postDTO.setImage(post.getImage());
        postDTO.setLikes(post.getLikes());
        postDTO.setUsersLiked(post.getLikedUser());

        return postDTO;
    }
}
