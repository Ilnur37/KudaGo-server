package com.example.server.web;

import com.example.server.entity.images.ImageFilmModel;
import com.example.server.payload.response.MessageResponse;
import com.example.server.services.images.ImageFilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
@CrossOrigin
public class ImageFilmController {
    @Autowired
    private ImageFilmService imageFilmService;

    @PostMapping("/{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") String postId,
                                                             @RequestParam("file") MultipartFile file) throws IOException {
        imageFilmService.uploadImageToPost(file, Long.parseLong(postId));
        return ResponseEntity.ok(new MessageResponse("Image Uploaded Successfully"));
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<ImageFilmModel> getImageToPost(@PathVariable("postId") String postId) {
        ImageFilmModel postImage = imageFilmService.getImageToPost(Long.parseLong(postId));
        return new ResponseEntity<>(postImage, HttpStatus.OK);
    }
}
