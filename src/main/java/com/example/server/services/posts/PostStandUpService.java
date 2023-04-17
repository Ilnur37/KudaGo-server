package com.example.server.services.posts;

import com.example.server.dto.PostStandUpDTO;
import com.example.server.entity.standUp.PostStandUp;
import com.example.server.exceptions.PostNotFoundException;
import com.example.server.repository.posts.PostStandUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostStandUpService {
    private final PostStandUpRepository postStandUpRepository;

    @Autowired
    public PostStandUpService(PostStandUpRepository postStandUpRepository) {
        this.postStandUpRepository = postStandUpRepository;
    }

    public PostStandUp createPost(PostStandUpDTO postDTO) {
        PostStandUp post = new PostStandUp();
        post.setTitle(postDTO.getTitle());
        post.setInfo(postDTO.getInfo());
        post.setShortInfo(postDTO.getShortInfo());
        post.setGenre(postDTO.getGenre());
        post.setExecutor(postDTO.getExecutor());
        post.setImage(postDTO.getImage());
        post.setLikes(0);

        return postStandUpRepository.save(post);
    }

    public List<PostStandUp> getAllPosts() {
        return postStandUpRepository.findAll();
    }

    public PostStandUp getPostById(Long postId) {
        return postStandUpRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post cannot be found"));
    }

    public PostStandUp likePost(Long postId, String username) {
        PostStandUp post = postStandUpRepository.findById(postId)
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
        return postStandUpRepository.save(post);
    }

    public void deletePost(Long postId) {
        PostStandUp post = getPostById(postId);
        postStandUpRepository.delete(post);
    }
}
