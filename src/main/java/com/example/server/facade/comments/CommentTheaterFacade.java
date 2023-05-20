package com.example.server.facade.comments;

import com.example.server.dto.comments.CommentTheaterDTO;
import com.example.server.entity.theater.CommentTheater;
import org.springframework.stereotype.Component;

@Component
public class CommentTheaterFacade {
    public CommentTheaterDTO commentToCommentTheaterDTO(CommentTheater comment) {
        CommentTheaterDTO commentDTO = new CommentTheaterDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setMessage(comment.getMessage());
        commentDTO.setUsername(comment.getUsername());

        return commentDTO;
    }
}
