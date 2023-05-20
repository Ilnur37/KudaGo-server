package com.example.server.web.post;

import com.example.server.dto.posts.PostTheaterDTO;
import com.example.server.entity.theater.PostTheater;
import com.example.server.facade.posts.PostTheaterFacade;
import com.example.server.payload.response.MessageResponse;
import com.example.server.services.posts.PostTheaterService;
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
@RequestMapping("api/post-theater")
@CrossOrigin
public class PostTheaterController {
    @Autowired
    private PostTheaterFacade postFacade;
    @Autowired
    private PostTheaterService postService;

    @GetMapping("/all/{sorted}")
    public ResponseEntity<List<PostTheaterDTO>> getAllPost(@PathVariable("sorted") String sorted) {
        List<PostTheaterDTO> postDTOList = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostTheaterDTO)
                .collect(Collectors.toList());

        if (!sorted.equals("default")) {
            postDTOList.sort((o1, o2) -> o1.getLikes() - o2.getLikes());

            if (sorted.equals("desc"))
                Collections.reverse(postDTOList);
        }

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @GetMapping("/info/{postId}")
    public ResponseEntity<PostTheaterDTO> getFullInfo(@PathVariable("postId") String postId) {
        PostTheater getPost = postService.getPostById(Long.parseLong(postId));
        PostTheaterDTO postDTO = postFacade.postToPostTheaterDTO(getPost);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updatePost(@Valid @RequestBody PostTheaterDTO postDTO) {
        PostTheater getPost = postService.getPostById(postDTO.getId());
        PostTheater updatedPost = postService.updatePost(getPost, postDTO);

        PostTheaterDTO updatedPostDTO = postFacade.postToPostTheaterDTO(updatedPost);
        return new ResponseEntity<>(updatedPostDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostTheaterDTO> likePost(@PathVariable("postId") String postId,
                                                   @PathVariable("username") String username) {
        PostTheater post = postService.likePost(Long.parseLong(postId), username);
        PostTheaterDTO postDTO = postFacade.postToPostTheaterDTO(post);

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
