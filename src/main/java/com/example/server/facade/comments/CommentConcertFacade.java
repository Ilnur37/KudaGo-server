package com.example.server.facade.comments;

import com.example.server.dto.comments.CommentConcertDTO;
import com.example.server.entity.concert.CommentConcert;
import org.springframework.stereotype.Component;

@Component
public class CommentConcertFacade {
    public CommentConcertDTO commentToCommentConcertDTO(CommentConcert comment) {
        CommentConcertDTO commentDTO = new CommentConcertDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setMessage(comment.getMessage());
        commentDTO.setUsername(comment.getUsername());

        return commentDTO;
    }
}
