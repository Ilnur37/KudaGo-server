package com.example.server.web.post;

import com.example.server.dto.posts.PostConcertDTO;
import com.example.server.entity.concert.PostConcert;
import com.example.server.facade.posts.PostConcertFacade;
import com.example.server.payload.response.MessageResponse;
import com.example.server.services.posts.PostConcertService;
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
@RequestMapping("api/post-concert")
@CrossOrigin
public class PostConcertController {
    @Autowired
    private PostConcertFacade postFacade;
    @Autowired
    private PostConcertService postService;

    @GetMapping("/all/{sorted}")
    public ResponseEntity<List<PostConcertDTO>> getAllPost(@PathVariable("sorted") String sorted) {
        List<PostConcertDTO> postDTOList = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostConcertDTO)
                .collect(Collectors.toList());

        if (!sorted.equals("default")) {
            Collections.sort(postDTOList, (o1, o2) -> o1.getLikes() - o2.getLikes());

            if (sorted.equals("desc"))
                Collections.reverse(postDTOList);
        }

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @GetMapping("/info/{postId}")
    public ResponseEntity<PostConcertDTO> getFullInfo(@PathVariable("postId") String postId) {
        PostConcert getPost = postService.getPostById(Long.parseLong(postId));
        PostConcertDTO postDTO = postFacade.postToPostConcertDTO(getPost);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updatePost(@Valid @RequestBody PostConcertDTO postDTO) {
        PostConcert getPost = postService.getPostById(postDTO.getId());
        PostConcert updatedPost = postService.updatePost(getPost, postDTO);

        PostConcertDTO updatedPostDTO = postFacade.postToPostConcertDTO(updatedPost);
        return new ResponseEntity<>(updatedPostDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostConcertDTO> likePost(@PathVariable("postId") String postId,
                                                @PathVariable("username") String username) {
        PostConcert post = postService.likePost(Long.parseLong(postId), username);
        PostConcertDTO postDTO = postFacade.postToPostConcertDTO(post);

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
