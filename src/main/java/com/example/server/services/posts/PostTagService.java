package com.example.server.services.posts;

import com.example.server.dto.posts.PostTagDTO;
import com.example.server.entity.tags.PostTag;
import com.example.server.exceptions.PostNotFoundException;
import com.example.server.repository.posts.PostTagRepository;
import com.example.server.services.UserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PostTagService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final PostTagRepository postRepository;

    @Autowired
    public PostTagService(PostTagRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostTag> getAllPosts() {

        return postRepository.findAll();
    }

    public List<PostTag> getPostsByGenre(String genre) {
        return postRepository.findPostTagByGenre(genre);
    }

    public PostTag getPostById(Long postId) {
        PostTag post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post cannot be found"));
        LOG.info(post.getTitle());
        return post;
    }

    public PostTag updatePost(PostTag post, PostTagDTO postDTO) {
        post.setTitle(postDTO.getTitle());
        post.setInfo(postDTO.getInfo());
        post.setShortInfo(postDTO.getShortInfo());
        post.setGenre(postDTO.getGenre());
        post.setAddress(postDTO.getAddress());
        post.setMainImage(postDTO.getMainImage());

        return postRepository.save(post);
    }

    public PostTag likePost(Long postId, String username) {
        PostTag post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        Optional<String> userLiked = post.getLikedUser()
                .stream()
                .filter(u -> u.equals(username)).findAny();

        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUser().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUser().add(username);
        }
        postRepository.save(post);
        return post;
    }

    public void deletePost(Long postId) {
        PostTag post = getPostById(postId);
        postRepository.delete(post);
    }

    public void createPost() throws IOException {
        for (int i = 2; i < 3; i++) {
            String ref = null;
            String titleEl = null;
            if (i == 0) {
                ref = "https://kudago.com/msk/activity/place-for-date/?type=event";
                titleEl = "post post-rect post-city post-feed-featured ";
            }
            if (i == 1) {
                ref = "https://kudago.com/msk/activity/things-to-do-with-girlfriend/";
                titleEl = "post post-rect post-city";
            }
            if (i == 2) {
                ref = "https://kudago.com/msk/activity/celebrate-birthday/";
                titleEl = "post post-rect post-city";
            }
            if (i == 3) {
                ref = "https://kaverafisha.ru/moscow/places?type=artplace";
            }
            if (i == 4) {
                ref = "https://kaverafisha.ru/moscow/places?type=bar";
            }

            Document doc = Jsoup
                    .connect(ref)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                    .get();

            Elements postTitleEl = doc.
                    getElementsByClass(titleEl);

            for (Element el : postTitleEl) {
                String detailsLink = el
                        .getElementsByClass("post-title-link")
                        .attr("href");
                PostTag post = new PostTag();

                post.setTitle(el
                        .getElementsByClass("post-title-link")
                        .text());

                post.setShortInfo(el
                        .getElementsByClass("post-description")
                        .text());

                post.setLikes(0);
                post.setReferenceInfo("/tag/info/");

                post.setDetailsLink(detailsLink);

                postRepository.save(post);
            }
        }
    }

    public void createPostDetails() throws IOException {
        List<PostTag> allPosts = postRepository.findAll();
        for (PostTag post : allPosts) {
            if (post.getInfo() != null) continue;
            if (post.getDetailsLink() == null) continue;
            Document postDetails = Jsoup.connect(post.getDetailsLink()).get();

            post.setMainImage(postDetails
                    .getElementsByClass("post-big-preview-image")
                    .attr("src"));

            post.setInfo(postDetails
                    .getElementsByClass("post-big-text")
                    .text());

            post.setAddress(postDetails
                    .getElementsByClass("addressItem addressItem--single")
                    .text());

            postRepository.save(post);
        }
    }
}
