package com.example.server.services.comments;

import com.example.server.dto.comments.CommentTheaterDTO;
import com.example.server.entity.User;
import com.example.server.entity.theater.CommentTheater;
import com.example.server.entity.theater.PostTheater;
import com.example.server.exceptions.PostNotFoundException;
import com.example.server.repository.UserRepository;
import com.example.server.repository.comments.CommentTheaterRepository;
import com.example.server.repository.posts.PostTheaterRepository;
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
public class CommentTheaterService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final CommentTheaterRepository commentRepository;
    private final PostTheaterRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentTheaterService(CommentTheaterRepository commentRepository, PostTheaterRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CommentTheater saveComment(Long postId, CommentTheaterDTO commentDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        PostTheater post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found username: " + user.getEmail()));

        CommentTheater comment = new CommentTheater();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());

        LOG.info("Saving comment fot Post: {}", post.getId());
        return  commentRepository.save(comment);
    }

    public List<CommentTheater> getAllCommentsForPost(Long postId){
        PostTheater post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        List<CommentTheater> comments = commentRepository.findAllByPost(post);

        return comments;
    }

    public void deleteComment (Long commentId) {
        Optional<CommentTheater> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username " + username));
    }
}
