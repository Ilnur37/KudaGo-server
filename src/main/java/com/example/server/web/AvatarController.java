package com.example.server.web;

import com.example.server.entity.Avatar;
import com.example.server.payload.response.MessageResponse;
import com.example.server.services.images.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/avatar")
@CrossOrigin
public class AvatarController {
    @Autowired
    private AvatarService avatarService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadAvatarToUser(@RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        avatarService.uploadImageToUser(file, principal);
        return ResponseEntity.ok(new MessageResponse("Image Uploaded Successfully"));
    }

    @GetMapping("/profileImage")
    public ResponseEntity<Avatar> getImageForUser(Principal principal) {
        Avatar avatar = avatarService.getImageToUser(principal);
        return new ResponseEntity<>(avatar, HttpStatus.OK);
    }
}
