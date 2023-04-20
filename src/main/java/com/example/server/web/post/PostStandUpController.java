package com.example.server.web.post;

import com.example.server.dto.PostStandUpDTO;
import com.example.server.entity.standUp.PostStandUp;
import com.example.server.facade.PostStandUpFacade;
import com.example.server.payload.response.MessageResponse;
import com.example.server.services.posts.PostStandUpService;
import com.example.server.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/postStandUp")
@CrossOrigin
public class PostStandUpController {
    @Autowired
    private PostStandUpFacade postFacade;
    @Autowired
    private PostStandUpService postService;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostStandUpDTO postDTO,
                                             BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        PostStandUp postStandUp = postService.createPost(postDTO);
        PostStandUpDTO createdPost = postFacade.postToPostStandUpDTO(postStandUp);

        return new ResponseEntity<>(createdPost, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostStandUpDTO>> getAllPost() {
        List<PostStandUpDTO> postDTOList = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostStandUpDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostStandUpDTO> likePost(@PathVariable("postId") String postId,
                                                @PathVariable("username") String username) {
        PostStandUp postStandUp = postService.likePost(Long.parseLong(postId), username);
        PostStandUpDTO postStandUpDTO = postFacade.postToPostStandUpDTO(postStandUp);

        return new ResponseEntity<>(postStandUpDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId) {
        postService.deletePost(Long.parseLong(postId));
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}
