package com.example.server.facade.posts;

import com.example.server.dto.posts.PostTopDTO;
import com.example.server.entity.top10.PostTop;
import org.springframework.stereotype.Component;

@Component
public class PostTopFacade {
    public PostTopDTO postToPostTopDTO(PostTop post) {
        PostTopDTO postDTO = new PostTopDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setTitleInfo(post.getTitleInfo());
        postDTO.setInfo(post.getInfo());
        postDTO.setShortInfo(post.getShortInfo());
        postDTO.setRating(post.getRating());
        postDTO.setGenre(post.getGenre());
        postDTO.setExecutor(post.getExecutor());
        postDTO.setAddress(post.getAddress());
        postDTO.setMetro(post.getMetro());
        postDTO.setImage(post.getImage());
        postDTO.setMainImage(post.getMainImage());
        postDTO.setLikes(post.getLikes());
        postDTO.setUsersLiked(post.getLikedUser());

        return postDTO;
    }
}
