package com.example.server.web.comment;

import com.example.server.dto.CommentStandUpDTO;
import com.example.server.entity.standUp.CommentStandUp;
import com.example.server.facade.CommentStandUpFacade;
import com.example.server.payload.response.MessageResponse;
import com.example.server.services.comments.CommentStandUpService;
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
@RequestMapping("/api/commentStandUp")
@CrossOrigin
public class CommentStandUpController {
    @Autowired
    private CommentStandUpService commentService;
    @Autowired
    private CommentStandUpFacade commentFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentStandUpDTO commentDTO,
                                               @PathVariable("postId") String postId,
                                               BindingResult bindingResult,
                                               Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        CommentStandUp comment = commentService.saveComment(Long.parseLong(postId), commentDTO, principal);
        CommentStandUpDTO createdComment = commentFacade.commentToCommentStandUpFacade(comment);

        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentStandUpDTO>> getAllCommentsToPost(@PathVariable("postId") String postId) {
        List<CommentStandUpDTO> commentDTOList = commentService.getAllCommentsForPost(Long.parseLong(postId))
                .stream()
                .map(commentFacade::commentToCommentStandUpFacade)
                .collect(Collectors.toList());

        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}
