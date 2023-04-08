package com.example.server.facade;

import com.example.server.dto.CommentFilmDTO;
import com.example.server.entity.comments.CommentFilm;
import org.springframework.stereotype.Component;

@Component
public class CommentFilmFacade {
    public CommentFilmDTO commentToCommentDTO(CommentFilm commentFilm) {
        CommentFilmDTO commentFilmDTO = new CommentFilmDTO();
        commentFilmDTO.setId(commentFilm.getId());
        commentFilmDTO.setMessage(commentFilm.getMessage());
        commentFilmDTO.setUsername(commentFilm.getUsername());

        return commentFilmDTO;
    }
}

