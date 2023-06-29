package com.example.server.facade.posts;

import com.example.server.dto.posts.PostTagDTO;
import com.example.server.entity.tags.PostTag;
import org.springframework.stereotype.Component;

@Component
public class PostTagFacade {
    public PostTagDTO postToPostTagDTO(PostTag post) {
        PostTagDTO postDTO = new PostTagDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setInfo(post.getInfo());
        postDTO.setShortInfo(post.getShortInfo());
        postDTO.setGenre(post.getGenre());
        postDTO.setAddress(post.getAddress());
        postDTO.setMainImage(post.getMainImage());
        postDTO.setLikes(post.getLikes());
        postDTO.setUsersLiked(post.getLikedUser());
        postDTO.setReferenceInfo(post.getReferenceInfo());

        return postDTO;
    }
}
