package com.example.server.web.comment;

import com.example.server.dto.comments.CommentTagDTO;
import com.example.server.entity.tags.CommentTag;
import com.example.server.facade.comments.CommentTagFacade;
import com.example.server.payload.response.MessageResponse;
import com.example.server.services.comments.CommentTagService;
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
@RequestMapping("/api/comment-tag")
@CrossOrigin
public class CommentTagController {
    @Autowired
    private CommentTagService commentService;
    @Autowired
    private CommentTagFacade commentFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentTagDTO commentDTO,
                                               @PathVariable("postId") String postId,
                                               BindingResult bindingResult,
                                               Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        CommentTag comment = commentService.saveComment(Long.parseLong(postId), commentDTO, principal);
        CommentTagDTO createdComment = commentFacade.commentToCommentTagFacade(comment);

        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentTagDTO>> getAllCommentsToPost(@PathVariable("postId") String postId) {
        List<CommentTagDTO> commentDTOList = commentService.getAllCommentsForPost(Long.parseLong(postId))
                .stream()
                .map(commentFacade::commentToCommentTagFacade)
                .collect(Collectors.toList());

        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}
