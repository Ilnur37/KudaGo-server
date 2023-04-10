package com.example.server.web;

import com.example.server.dto.CommentFilmDTO;
import com.example.server.entity.comments.CommentFilm;
import com.example.server.facade.CommentFilmFacade;
import com.example.server.payload.response.MessageResponse;
import com.example.server.services.comments.CommentFilmService;
import com.example.server.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commentfilm")
@CrossOrigin
public class CommentFilmController {
    @Autowired
    private CommentFilmService commentFilmService;
    @Autowired
    private CommentFilmFacade commentFilmFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentFilmDTO commentFilmDTO,
                                                @PathVariable("postId") String postId,
                                                BindingResult bindingResult,
                                                Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        CommentFilm comment = commentFilmService.saveComment(Long.parseLong(postId), commentFilmDTO, principal);
        CommentFilmDTO createdComment = commentFilmFacade.commentToCommentFilmDTO(comment);

        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentFilmDTO>> getAllCommentsToPost(@PathVariable("postId") String postId) {
        List<CommentFilmDTO> commentDTOList = commentFilmService.getAllCommentsForPost(Long.parseLong(postId))
                .stream()
                .map(commentFilmFacade::commentToCommentFilmDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {
        commentFilmService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}
