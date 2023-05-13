package com.example.server.web.post;

import com.example.server.dto.PostFilmDTO;
import com.example.server.entity.film.PostFilm;
import com.example.server.facade.PostFilmFacade;
import com.example.server.payload.response.MessageResponse;
import com.example.server.services.posts.PostFilmService;
import com.example.server.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/postFilm")
@CrossOrigin
public class PostFilmController {
    @Autowired
    private PostFilmFacade postFacade;
    @Autowired
    private PostFilmService postService;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostFilmDTO postDTO,
                                             BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        PostFilm postFilm = postService.createPost(postDTO);
        PostFilmDTO createdPost = postFacade.postToPostFilmDTO(postFilm);

        return new ResponseEntity<>(createdPost, HttpStatus.OK);
    }

    @GetMapping("/all/{sorted}")
    public ResponseEntity<List<PostFilmDTO>> getAllPost(@PathVariable("sorted") String sorted) {
        List<PostFilmDTO> postDTOList = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostFilmDTO)
                .collect(Collectors.toList());

        if (!sorted.equals("default")) {
            Collections.sort(postDTOList, (o1, o2) -> o1.getLikes() - o2.getLikes());

            if (sorted.equals("desc"))

                Collections.reverse(postDTOList);
        }

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @GetMapping("/info/{postId}")
    public ResponseEntity<PostFilmDTO> getFullInfo(@PathVariable("postId") String postId) {
        PostFilm postFilm = postService.getPostById(Long.parseLong(postId));
        PostFilmDTO postFilmDTO = postFacade.postToPostFilmDTO(postFilm);

        return new ResponseEntity<>(postFilmDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updatePost(@Valid @RequestBody PostFilmDTO postDTO) {
        PostFilm postFilm = postService.getPostById(postDTO.getId());
        PostFilm post = postService.updatePost(postFilm, postDTO);

        PostFilmDTO updatedPost = postFacade.postToPostFilmDTO(post);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostFilmDTO> likePost(@PathVariable("postId") String postId,
                                            @PathVariable("username") String username) {
        PostFilm postFilm = postService.likePost(Long.parseLong(postId), username);
        PostFilmDTO postFilmDTO = postFacade.postToPostFilmDTO(postFilm);

        return new ResponseEntity<>(postFilmDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId) {
        postService.deletePost(Long.parseLong(postId));
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }

    @PostMapping("/parser")
    public ResponseEntity<Object> createPost() throws IOException {
        postService.createFilm();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/parser/fullParser")
    public ResponseEntity<Object> createPostDetails() throws IOException {
        postService.createFilmDetails();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
