package com.example.server.facade.posts;

import com.example.server.dto.posts.PostStandUpDTO;
import com.example.server.entity.standUp.PostStandUp;
import org.springframework.stereotype.Component;

@Component
public class PostStandUpFacade {
    public PostStandUpDTO postToPostStandUpDTO(PostStandUp post) {
        PostStandUpDTO postDTO = new PostStandUpDTO();
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
