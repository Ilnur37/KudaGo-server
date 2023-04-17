package com.example.server.services.images;

import com.example.server.entity.User;
import com.example.server.entity.Avatar;
import com.example.server.repository.UserRepository;
import com.example.server.repository.images.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Service
public class AvatarService extends ImageService {

    private final AvatarRepository avatarRepository;

    @Autowired
    public AvatarService(UserRepository userRepository, AvatarRepository avatarRepository) {
        super(userRepository);
        this.avatarRepository = avatarRepository;
    }

    public Avatar uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
        LOG.info("Uploading image profile to User {}", user.getUsername());

        Avatar userProfileImage = avatarRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            avatarRepository.delete(userProfileImage);
        }

        Avatar avatar = new Avatar();
        avatar.setUserId(user.getId());
        avatar.setImageBytes(compressBytes(file.getBytes()));
        avatar.setName(file.getOriginalFilename());
        return avatarRepository.save(avatar);
    }

    public Avatar getImageToUser(Principal principal) {
        User user = getUserByPrincipal(principal);

        Avatar avatar = avatarRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(avatar)) {
            avatar.setImageBytes(decompressBytes(avatar.getImageBytes()));
        }

        return avatar;
    }
}
