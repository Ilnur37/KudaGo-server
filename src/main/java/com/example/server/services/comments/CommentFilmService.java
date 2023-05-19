package com.example.server.services.comments;

import com.example.server.dto.comments.CommentFilmDTO;
import com.example.server.entity.User;
import com.example.server.entity.film.CommentFilm;
import com.example.server.entity.film.PostFilm;
import com.example.server.exceptions.PostNotFoundException;
import com.example.server.repository.comments.CommentFilmRepository;
import com.example.server.repository.posts.PostFilmRepository;
import com.example.server.repository.UserRepository;
import com.example.server.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentFilmService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final CommentFilmRepository commentRepository;
    private final PostFilmRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentFilmService(CommentFilmRepository commentRepository, PostFilmRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CommentFilm saveComment(Long postId, CommentFilmDTO commentDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        PostFilm post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found username: " + user.getEmail()));

        CommentFilm comment = new CommentFilm();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());

        LOG.info("Saving comment fot Post: {}", post.getId());
        return  commentRepository.save(comment);
    }

    public List<CommentFilm> getAllCommentsForPost(Long postId){
        PostFilm post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        List<CommentFilm> comments = commentRepository.findAllByPost(post);

        return comments;
    }

    public void deleteComment (Long commentId) {
        Optional<CommentFilm> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username " + username));
    }
}
