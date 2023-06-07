package com.example.server.web.post;

import com.example.server.dto.posts.PostStandUpDTO;
import com.example.server.entity.standUp.PostStandUp;
import com.example.server.facade.posts.PostStandUpFacade;
import com.example.server.payload.response.MessageResponse;
import com.example.server.services.posts.PostStandUpService;
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
@RequestMapping("api/post-standUp")
@CrossOrigin
public class PostStandUpController {
    @Autowired
    private PostStandUpFacade postFacade;
    @Autowired
    private PostStandUpService postService;

    @GetMapping("/all/{sorted}")
    public ResponseEntity<List<PostStandUpDTO>> getAllPost(@PathVariable("sorted") String sorted) {
        List<PostStandUpDTO> postDTOList = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostStandUpDTO)
                .collect(Collectors.toList());

        if (!sorted.equals("default")) {
            Collections.sort(postDTOList, (o1, o2) -> o1.getLikes() - o2.getLikes());

            if (sorted.equals("desc"))
                Collections.reverse(postDTOList);
        }

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @GetMapping("/info/{postId}")
    public ResponseEntity<PostStandUpDTO> getFullInfo(@PathVariable("postId") String postId) {
        PostStandUp getPost = postService.getPostById(Long.parseLong(postId));
        PostStandUpDTO postDTO = postFacade.postToPostStandUpDTO(getPost);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updatePost(@Valid @RequestBody PostStandUpDTO postDTO) {
        PostStandUp getPost = postService.getPostById(postDTO.getId());
        PostStandUp updatedPost = postService.updatePost(getPost, postDTO);

        PostStandUpDTO updatedPostDTO = postFacade.postToPostStandUpDTO(updatedPost);
        return new ResponseEntity<>(updatedPostDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostStandUpDTO> likePost(@PathVariable("postId") String postId,
                                                @PathVariable("username") String username) {
        PostStandUp post = postService.likePost(Long.parseLong(postId), username);
        PostStandUpDTO postDTO = postFacade.postToPostStandUpDTO(post);

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
