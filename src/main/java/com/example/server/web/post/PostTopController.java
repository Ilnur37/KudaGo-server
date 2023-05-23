package com.example.server.web.post;

import com.example.server.dto.posts.PostTopDTO;
import com.example.server.entity.top10.PostTop;
import com.example.server.facade.posts.PostTopFacade;
import com.example.server.payload.response.MessageResponse;
import com.example.server.services.posts.PostTopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/post-top")
@CrossOrigin
public class PostTopController {
    @Autowired
    private PostTopFacade postFacade;
    @Autowired
    private PostTopService postService;

    @GetMapping("/all")
    public ResponseEntity<List<PostTopDTO>> getAllPost() {
        List<PostTopDTO> postDTOList = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostTopDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @GetMapping("/info/{postId}")
    public ResponseEntity<PostTopDTO> getFullInfo(@PathVariable("postId") String postId) {
        PostTop getPost = postService.getPostById(Long.parseLong(postId));
        PostTopDTO postDTO = postFacade.postToPostTopDTO(getPost);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updatePost(@Valid @RequestBody PostTopDTO postDTO) {
        PostTop getPost = postService.getPostById(postDTO.getId());
        PostTop updatedPost = postService.updatePost(getPost, postDTO);

        PostTopDTO updatedPostDTO = postFacade.postToPostTopDTO(updatedPost);
        return new ResponseEntity<>(updatedPostDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostTopDTO> likePost(@PathVariable("postId") String postId,
                                                @PathVariable("username") String username) {
        PostTop post = postService.likePost(Long.parseLong(postId), username);
        PostTopDTO postDTO = postFacade.postToPostTopDTO(post);

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
