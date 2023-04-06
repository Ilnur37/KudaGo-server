package com.example.server.services;

import com.example.server.dto.PostFilmDTO;
import com.example.server.entity.ImageModel;
import com.example.server.entity.posts.PostFilm;
import com.example.server.exceptions.PostNotFoundException;
import com.example.server.repository.ImageRepository;
import com.example.server.repository.PostFilmRepository;
import com.example.server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostFilmService {
    public static final Logger LOG = LoggerFactory.getLogger(PostFilmService.class);

    private final PostFilmRepository postFilmRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public PostFilmService(PostFilmRepository postFilmRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.postFilmRepository = postFilmRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public PostFilm createPost(PostFilmDTO postDTO, Principal principal) {
        PostFilm post = new PostFilm();
        post.setTitle(postDTO.getTitle());
        post.setInfo(postDTO.getInfo());
        post.setShortInfo(postDTO.getShortInfo());
        post.setGenre(postDTO.getGenre());
        post.setCinema(postDTO.getCinema());
        post.setLikes(0);

        return postFilmRepository.save(post);
    }

    public List<PostFilm> getAllPosts() {
        return postFilmRepository.findAll();
    }

    public PostFilm getPostById(Long postId) {
        return postFilmRepository.findPostById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post cannot be found"));
    }

    public PostFilm likePost(Long postId, String username) {
        PostFilm post = postFilmRepository.findById(postId)
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
        return postFilmRepository.save(post);
    }

    public void deletePost(Long postId) {
        PostFilm post = getPostById(postId);
        Optional<ImageModel> imageModel = imageRepository.findByPostId(post.getId());
        postFilmRepository.delete(post);
        imageModel.ifPresent(imageRepository::delete);
    }
}
