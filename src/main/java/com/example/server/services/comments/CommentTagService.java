package com.example.server.services.comments;

import com.example.server.dto.comments.CommentTagDTO;
import com.example.server.entity.User;
import com.example.server.entity.tags.CommentTag;
import com.example.server.entity.tags.PostTag;
import com.example.server.exceptions.PostNotFoundException;
import com.example.server.repository.UserRepository;
import com.example.server.repository.comments.CommentTagRepository;
import com.example.server.repository.comments.CommentTopRepository;
import com.example.server.repository.posts.PostTagRepository;
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
public class CommentTagService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final CommentTagRepository commentRepository;
    private final PostTagRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentTagService(CommentTagRepository commentRepository, PostTagRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CommentTag saveComment(Long postId, CommentTagDTO commentDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        PostTag post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found username: " + user.getEmail()));

        CommentTag comment = new CommentTag();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());

        LOG.info("Saving comment fot Post: {}", post.getId());
        return commentRepository.save(comment);
    }

    public List<CommentTag> getAllCommentsForPost(Long postId) {
        PostTag post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        List<CommentTag> comments = commentRepository.findAllByPost(post);

        return comments;
    }

    public void deleteCommentStandUp (Long commentId) {
        Optional<CommentTag> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    public void deleteComment (Long commentId) {
        Optional<CommentTag> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username " + username));
    }
}
