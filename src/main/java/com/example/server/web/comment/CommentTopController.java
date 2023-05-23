package com.example.server.web.comment;

import com.example.server.dto.comments.CommentTopDTO;
import com.example.server.entity.top10.CommentTop;
import com.example.server.facade.comments.CommentTopFacade;
import com.example.server.payload.response.MessageResponse;
import com.example.server.services.comments.CommentTopService;
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
@RequestMapping("/api/comment-top")
@CrossOrigin
public class CommentTopController {
    @Autowired
    private CommentTopService commentService;
    @Autowired
    private CommentTopFacade commentFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentTopDTO commentDTO,
                                               @PathVariable("postId") String postId,
                                               BindingResult bindingResult,
                                               Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        CommentTop comment = commentService.saveComment(Long.parseLong(postId), commentDTO, principal);
        CommentTopDTO createdComment = commentFacade.commentToCommentTopFacade(comment);

        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentTopDTO>> getAllCommentsToPost(@PathVariable("postId") String postId) {
        List<CommentTopDTO> commentDTOList = commentService.getAllCommentsForPost(Long.parseLong(postId))
                .stream()
                .map(commentFacade::commentToCommentTopFacade)
                .collect(Collectors.toList());

        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}
