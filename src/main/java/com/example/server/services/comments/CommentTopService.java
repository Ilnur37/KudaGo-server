package com.example.server.services.comments;

import com.example.server.dto.comments.CommentTopDTO;
import com.example.server.entity.User;
import com.example.server.entity.top10.CommentTop;
import com.example.server.entity.top10.PostTop;
import com.example.server.exceptions.PostNotFoundException;
import com.example.server.repository.UserRepository;
import com.example.server.repository.comments.CommentTopRepository;
import com.example.server.repository.posts.PostTopRepository;
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
public class CommentTopService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final CommentTopRepository commentRepository;
    private final PostTopRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentTopService(CommentTopRepository commentRepository, PostTopRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CommentTop saveComment(Long postId, CommentTopDTO commentDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        PostTop post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found username: " + user.getEmail()));

        CommentTop comment = new CommentTop();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());

        LOG.info("Saving comment fot Post: {}", post.getId());
        return commentRepository.save(comment);
    }

    public List<CommentTop> getAllCommentsForPost(Long postId) {
        PostTop post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        List<CommentTop> comments = commentRepository.findAllByPost(post);

        return comments;
    }

    public void deleteCommentStandUp (Long commentId) {
        Optional<CommentTop> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    public void deleteComment (Long commentId) {
        Optional<CommentTop> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username " + username));
    }
}
