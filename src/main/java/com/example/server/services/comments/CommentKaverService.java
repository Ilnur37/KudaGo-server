package com.example.server.services.comments;

import com.example.server.dto.comments.CommentKaverDTO;
import com.example.server.entity.User;
import com.example.server.entity.fromKaver.CommentKaver;
import com.example.server.entity.fromKaver.PostKaver;
import com.example.server.exceptions.PostNotFoundException;
import com.example.server.repository.UserRepository;
import com.example.server.repository.comments.CommentKaverRepository;
import com.example.server.repository.posts.PostKaverRepository;
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
public class CommentKaverService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final CommentKaverRepository commentRepository;
    private final PostKaverRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentKaverService(CommentKaverRepository commentRepository, PostKaverRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CommentKaver saveComment(Long postId, CommentKaverDTO commentDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        PostKaver post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found username: " + user.getEmail()));

        CommentKaver comment = new CommentKaver();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());

        LOG.info("Saving comment fot Post: {}", post.getId());
        return commentRepository.save(comment);
    }

    public List<CommentKaver> getAllCommentsForPost(Long postId) {
        PostKaver post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        List<CommentKaver> comments = commentRepository.findAllByPost(post);

        return comments;
    }

    public void deleteCommentStandUp (Long commentId) {
        Optional<CommentKaver> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    public void deleteComment (Long commentId) {
        Optional<CommentKaver> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username " + username));
    }
}
