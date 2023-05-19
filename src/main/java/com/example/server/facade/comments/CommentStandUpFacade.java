package com.example.server.facade.comments;

import com.example.server.dto.comments.CommentStandUpDTO;
import com.example.server.entity.standUp.CommentStandUp;
import org.springframework.stereotype.Component;

@Component
public class CommentStandUpFacade {
    public CommentStandUpDTO commentToCommentStandUpFacade(CommentStandUp commentStandUp) {
        CommentStandUpDTO commentStandUpDTO = new CommentStandUpDTO();
        commentStandUpDTO.setId(commentStandUp.getId());
        commentStandUpDTO.setUsername(commentStandUp.getUsername());
        commentStandUpDTO.setMessage(commentStandUp.getMessage());

        return commentStandUpDTO;
    }
}
