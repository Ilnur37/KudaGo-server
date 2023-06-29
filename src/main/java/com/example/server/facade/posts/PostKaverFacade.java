package com.example.server.facade.posts;

import com.example.server.dto.posts.PostKaverDTO;
import com.example.server.entity.fromKaver.PostKaver;
import org.springframework.stereotype.Component;

@Component
public class PostKaverFacade {
    public PostKaverDTO postToPostKaverDTO(PostKaver post) {
        PostKaverDTO postDTO = new PostKaverDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setInfo(post.getInfo());
        postDTO.setGenre(post.getGenre());
        postDTO.setAddress(post.getAddress());
        postDTO.setImage(post.getImage());
        postDTO.setMainImage(post.getMainImage());
        postDTO.setLikes(post.getLikes());
        postDTO.setUsersLiked(post.getLikedUser());
        postDTO.setReferenceInfo(post.getReferenceInfo());

        return postDTO;
    }
}
