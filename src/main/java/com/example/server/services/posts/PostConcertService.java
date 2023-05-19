package com.example.server.services.posts;

import com.example.server.dto.posts.PostConcertDTO;
import com.example.server.entity.concert.PostConcert;
import com.example.server.exceptions.PostNotFoundException;
import com.example.server.repository.posts.PostConcertRepository;
import com.example.server.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PostConcertService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final PostConcertRepository postRepository;

    @Autowired
    public PostConcertService(PostConcertRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostConcert> getAllPosts() {
        return postRepository.findAll();
    }

    public PostConcert getPostById(Long postId) {
        PostConcert post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post cannot be found"));
        LOG.info(post.getTitle());
        return post;
    }

    public PostConcert updatePost(PostConcert post, PostConcertDTO postDTO) {
        post.setTitle(postDTO.getTitle());
        post.setInfo(postDTO.getInfo());
        post.setShortInfo(postDTO.getShortInfo());
        post.setGenre(postDTO.getGenre());
        post.setAddress(postDTO.getAddress());
        post.setExecutor(postDTO.getExecutor());
        post.setImage(postDTO.getImage());

        return postRepository.save(post);
    }

    public PostConcert likePost(Long postId, String username) {
        PostConcert post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        Optional<String> userLiked = post.getLikedUser()
                .stream()
                .filter(u -> u.equals(username)).findAny();

        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUser().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUser().add(username);
        }
        return postRepository.save(post);
    }

    public void deletePost(Long postId) {
        PostConcert post = getPostById(postId);
        postRepository.delete(post);
    }

    public void createPost() throws IOException {
    }

    public void createPostDetails() throws IOException {
    }
}
