package com.example.server.facade.comments;

import com.example.server.dto.comments.CommentKaverDTO;
import com.example.server.entity.fromKaver.CommentKaver;
import org.springframework.stereotype.Component;

@Component
public class CommentKaverFacade {
    public CommentKaverDTO commentToCommentKaverFacade(CommentKaver commentStandUp) {
        CommentKaverDTO commentStandUpDTO = new CommentKaverDTO();
        commentStandUpDTO.setId(commentStandUp.getId());
        commentStandUpDTO.setUsername(commentStandUp.getUsername());
        commentStandUpDTO.setMessage(commentStandUp.getMessage());

        return commentStandUpDTO;
    }
}
