package com.example.server.services.posts;

import com.example.server.dto.PostFilmDTO;
import com.example.server.entity.film.PostFilm;
import com.example.server.exceptions.PostNotFoundException;
import com.example.server.repository.posts.PostFilmRepository;
import com.example.server.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostFilmService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final PostFilmRepository postFilmRepository;

    @Autowired
    public PostFilmService(PostFilmRepository postFilmRepository) {
        this.postFilmRepository = postFilmRepository;
    }

    public PostFilm createPost(PostFilmDTO postDTO) {
        PostFilm post = new PostFilm();
        post.setTitle(postDTO.getTitle());
        post.setInfo(postDTO.getInfo());
        post.setShortInfo(postDTO.getShortInfo());
        post.setGenre(postDTO.getGenre());
        post.setCinema(postDTO.getCinema());
        post.setImage(postDTO.getImage());
        post.setLikes(0);

        return postFilmRepository.save(post);
    }

    public List<PostFilm> getAllPosts() {
        return postFilmRepository.findAll();
    }

    public PostFilm getPostById(Long postId) {
        PostFilm postFilm = postFilmRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post cannot be found"));
        LOG.info(postFilm.getTitle());
        return postFilm;

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
        postFilmRepository.delete(post);
    }
}
