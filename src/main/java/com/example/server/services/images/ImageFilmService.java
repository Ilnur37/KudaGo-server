package com.example.server.services.images;

import com.example.server.entity.images.ImageFilmModel;
import com.example.server.entity.posts.PostFilm;
import com.example.server.exceptions.ImageNotFoundException;
import com.example.server.repository.UserRepository;
import com.example.server.repository.images.ImageFilmRepository;
import com.example.server.repository.posts.PostFilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageFilmService extends ImageService {

    private final PostFilmRepository postFilmRepository;
    private final ImageFilmRepository imageFilmRepository;

    @Autowired
    public ImageFilmService(UserRepository userRepository, PostFilmRepository postFilmRepository, ImageFilmRepository imageFilmRepository) {
        super(userRepository);
        this.postFilmRepository = postFilmRepository;
        this.imageFilmRepository = imageFilmRepository;
    }

    public ImageFilmModel uploadImageToPost(MultipartFile file, Long postId) throws IOException {
        PostFilm post = postFilmRepository.findById(postId).orElse(null);

        ImageFilmModel imageFilmModel = new ImageFilmModel();
        imageFilmModel.setPostId(post.getId());
        imageFilmModel.setImageBytes(file.getBytes());
        imageFilmModel.setImageBytes(compressBytes(file.getBytes()));
        imageFilmModel.setName(file.getOriginalFilename());
        LOG.info("Uploading image to Post {}", post.getId());

        return imageFilmRepository.save(imageFilmModel);
    }

    public ImageFilmModel getImageToPost(Long postId) {
        ImageFilmModel imageFilmModel = imageFilmRepository.findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("Cannot find image to Post: " + postId));
        if (!ObjectUtils.isEmpty(imageFilmModel)) {
            imageFilmModel.setImageBytes(decompressBytes(imageFilmModel.getImageBytes()));
        }

        return imageFilmModel;
    }

}
