package com.example.server.web.post;

import com.example.server.dto.posts.PostFilmDTO;
import com.example.server.entity.film.PostFilm;
import com.example.server.facade.posts.PostFilmFacade;
import com.example.server.payload.response.MessageResponse;
import com.example.server.services.posts.PostFilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/post-film")
@CrossOrigin
public class PostFilmController {
    @Autowired
    private PostFilmFacade postFacade;
    @Autowired
    private PostFilmService postService;

    @GetMapping("/all/{sortLike}/{sortGenre}")
    public ResponseEntity<List<PostFilmDTO>> getAllPost(@PathVariable("sortLike") String sortLike,
                                                        @PathVariable("sortGenre") String sortGenre) {
        List<PostFilmDTO> postDTOList = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostFilmDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postService.getFilteredPosts(postDTOList, sortLike, sortGenre), HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostFilmDTO> getFullInfo(@PathVariable("postId") String postId) {
        PostFilm getPost = postService.getPostById(Long.parseLong(postId));
        PostFilmDTO postDTO = postFacade.postToPostFilmDTO(getPost);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updatePost(@Valid @RequestBody PostFilmDTO postDTO) {
        PostFilm getPost = postService.getPostById(postDTO.getId());
        PostFilm updatedPost = postService.updatePost(getPost, postDTO);

        PostFilmDTO updatedPostDTO = postFacade.postToPostFilmDTO(updatedPost);
        return new ResponseEntity<>(updatedPostDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostFilmDTO> likePost(@PathVariable("postId") String postId,
                                            @PathVariable("username") String username) {
        PostFilm post = postService.likePost(Long.parseLong(postId), username);
        PostFilmDTO postDTO = postFacade.postToPostFilmDTO(post);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId) {
        postService.deletePost(Long.parseLong(postId));
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }

    @PostMapping("/parser")
    public ResponseEntity<Object> createPost() throws IOException {
        postService.createPost();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/parser/full-parser")
    public ResponseEntity<Object> createPostDetails() throws IOException {
        postService.createPostDetails();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
