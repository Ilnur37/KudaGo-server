package com.example.server.facade.comments;

import com.example.server.dto.comments.CommentTagDTO;
import com.example.server.entity.tags.CommentTag;
import org.springframework.stereotype.Component;

@Component
public class CommentTagFacade {
    public CommentTagDTO commentToCommentTagFacade(CommentTag commentStandUp) {
        CommentTagDTO commentStandUpDTO = new CommentTagDTO();
        commentStandUpDTO.setId(commentStandUp.getId());
        commentStandUpDTO.setUsername(commentStandUp.getUsername());
        commentStandUpDTO.setMessage(commentStandUp.getMessage());

        return commentStandUpDTO;
    }
}
