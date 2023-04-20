package com.example.server.services.comments;

import com.example.server.dto.CommentStandUpDTO;
import com.example.server.entity.User;
import com.example.server.entity.standUp.CommentStandUp;
import com.example.server.entity.standUp.PostStandUp;
import com.example.server.exceptions.PostNotFoundException;
import com.example.server.repository.UserRepository;
import com.example.server.repository.comments.CommentStandUpRepository;
import com.example.server.repository.posts.PostStandUpRepository;
import com.example.server.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentStandUpService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final CommentStandUpRepository commentRepository;
    private final PostStandUpRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentStandUpService(CommentStandUpRepository commentRepository, PostStandUpRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CommentStandUp saveComment(Long postId, CommentStandUpDTO commentDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        PostStandUp post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found username: " + user.getEmail()));

        CommentStandUp comment = new CommentStandUp();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());

        LOG.info("Saving comment fot Post: {}", post.getId());
        return commentRepository.save(comment);
    }

    public List<CommentStandUp> getAllCommentsForPost(Long postId) {
        PostStandUp post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        List<CommentStandUp> comments = commentRepository.findAllByPost(post);

        return comments;
    }

    public void deleteCommentStandUp (Long commentId) {
        Optional<CommentStandUp> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    public void deleteComment (Long commentId) {
        Optional<CommentStandUp> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username " + username));
    }
}
