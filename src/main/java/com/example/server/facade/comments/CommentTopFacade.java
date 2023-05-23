package com.example.server.facade.comments;

import com.example.server.dto.comments.CommentTopDTO;
import com.example.server.entity.top10.CommentTop;
import org.springframework.stereotype.Component;

@Component
public class CommentTopFacade {
    public CommentTopDTO commentToCommentTopFacade(CommentTop commentTop) {
        CommentTopDTO commentStandUpDTO = new CommentTopDTO();
        commentStandUpDTO.setId(commentTop.getId());
        commentStandUpDTO.setUsername(commentTop.getUsername());
        commentStandUpDTO.setMessage(commentTop.getMessage());

        return commentStandUpDTO;
    }
}
