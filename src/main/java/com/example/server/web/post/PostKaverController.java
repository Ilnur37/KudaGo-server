package com.example.server.web.post;

import com.example.server.dto.posts.PostKaverDTO;
import com.example.server.entity.fromKaver.PostKaver;
import com.example.server.facade.posts.PostKaverFacade;
import com.example.server.payload.response.MessageResponse;
import com.example.server.services.posts.PostKaverService;
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
@RequestMapping("api/post-kaver")
@CrossOrigin
public class PostKaverController {
    @Autowired
    private PostKaverFacade postFacade;
    @Autowired
    private PostKaverService postService;

    @GetMapping("/all/{genre}/{sorted}")
    public ResponseEntity<List<PostKaverDTO>> getAllPost(@PathVariable("genre") String genre,
                                                         @PathVariable("sorted") String sorted) {
        List<PostKaverDTO> postDTOList = postService.getPostsByGenre(genre)
                .stream()
                .map(postFacade::postToPostKaverDTO)
                .collect(Collectors.toList());

        if (!sorted.equals("default")) {
            Collections.sort(postDTOList, (o1, o2) -> o1.getLikes() - o2.getLikes());

            if (sorted.equals("desc"))
                Collections.reverse(postDTOList);
        }

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @GetMapping("/info/{postId}")
    public ResponseEntity<PostKaverDTO> getFullInfo(@PathVariable("postId") String postId) {
        PostKaver getPost = postService.getPostById(Long.parseLong(postId));
        PostKaverDTO postDTO = postFacade.postToPostKaverDTO(getPost);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updatePost(@Valid @RequestBody PostKaverDTO postDTO) {
        PostKaver getPost = postService.getPostById(postDTO.getId());
        PostKaver updatedPost = postService.updatePost(getPost, postDTO);

        PostKaverDTO updatedPostDTO = postFacade.postToPostKaverDTO(updatedPost);
        return new ResponseEntity<>(updatedPostDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostKaverDTO> likePost(@PathVariable("postId") String postId,
                                                @PathVariable("username") String username) {
        PostKaver post = postService.likePost(Long.parseLong(postId), username);
        PostKaverDTO postDTO = postFacade.postToPostKaverDTO(post);

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
