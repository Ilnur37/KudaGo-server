package com.example.server.services.comments;

import com.example.server.dto.comments.CommentConcertDTO;
import com.example.server.entity.User;
import com.example.server.entity.concert.CommentConcert;
import com.example.server.entity.concert.PostConcert;
import com.example.server.exceptions.PostNotFoundException;
import com.example.server.repository.UserRepository;
import com.example.server.repository.comments.CommentConcertRepository;
import com.example.server.repository.posts.PostConcertRepository;
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
public class CommentConcertService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final CommentConcertRepository commentRepository;
    private final PostConcertRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentConcertService(CommentConcertRepository commentRepository, PostConcertRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CommentConcert saveComment(Long postId, CommentConcertDTO commentDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        PostConcert post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found username: " + user.getEmail()));

        CommentConcert comment = new CommentConcert();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());

        LOG.info("Saving comment fot Post: {}", post.getId());
        return  commentRepository.save(comment);
    }

    public List<CommentConcert> getAllCommentsForPost(Long postId){
        PostConcert post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        List<CommentConcert> comments = commentRepository.findAllByPost(post);

        return comments;
    }

    public void deleteComment (Long commentId) {
        Optional<CommentConcert> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username " + username));
    }
}
