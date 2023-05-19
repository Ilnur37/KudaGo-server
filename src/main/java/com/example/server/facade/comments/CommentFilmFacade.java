package com.example.server.facade.comments;

import com.example.server.dto.comments.CommentFilmDTO;
import com.example.server.entity.film.CommentFilm;
import org.springframework.stereotype.Component;

@Component
public class CommentFilmFacade {
    public CommentFilmDTO commentToCommentFilmDTO(CommentFilm comment) {
        CommentFilmDTO commentDTO = new CommentFilmDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setMessage(comment.getMessage());
        commentDTO.setUsername(comment.getUsername());

        return commentDTO;
    }
}
