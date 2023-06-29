package com.example.server.web.post;

import com.example.server.dto.posts.PostTagDTO;
import com.example.server.entity.tags.PostTag;
import com.example.server.facade.posts.PostTagFacade;
import com.example.server.payload.response.MessageResponse;
import com.example.server.services.posts.PostTagService;
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
@RequestMapping("api/post-tag")
@CrossOrigin
public class PostTagController {
    @Autowired
    private PostTagFacade postFacade;
    @Autowired
    private PostTagService postService;

    @GetMapping("/all/{genre}/{sorted}")
    public ResponseEntity<List<PostTagDTO>> getAllPost(@PathVariable("genre") String genre,
                                                       @PathVariable("sorted") String sorted) {
        String gen = null;
        if (genre.equals("date-places")) gen = "Места для свиданий";
        if (genre.equals("with-girl")) gen = "С девушкой";
        if (genre.equals("with-friends")) gen = "С друзьями";
        if (genre.equals("birthday")) gen = "День рождения";
        if (genre.equals("corporate")) gen = "Корпоратив";
        List<PostTagDTO> postDTOList = postService.getPostsByGenre(gen)
                .stream()
                .map(postFacade::postToPostTagDTO)
                .collect(Collectors.toList());

        if (!sorted.equals("default")) {
            Collections.sort(postDTOList, (o1, o2) -> o1.getLikes() - o2.getLikes());

            if (sorted.equals("desc"))
                Collections.reverse(postDTOList);
        }

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @GetMapping("/info/{postId}")
    public ResponseEntity<PostTagDTO> getFullInfo(@PathVariable("postId") String postId) {
        PostTag getPost = postService.getPostById(Long.parseLong(postId));
        PostTagDTO postDTO = postFacade.postToPostTagDTO(getPost);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updatePost(@Valid @RequestBody PostTagDTO postDTO) {
        PostTag getPost = postService.getPostById(postDTO.getId());
        PostTag updatedPost = postService.updatePost(getPost, postDTO);

        PostTagDTO updatedPostDTO = postFacade.postToPostTagDTO(updatedPost);
        return new ResponseEntity<>(updatedPostDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostTagDTO> likePost(@PathVariable("postId") String postId,
                                                @PathVariable("username") String username) {
        PostTag post = postService.likePost(Long.parseLong(postId), username);
        PostTagDTO postDTO = postFacade.postToPostTagDTO(post);

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
