package com.example.server.services.posts;

import com.example.server.dto.posts.PostKaverDTO;
import com.example.server.entity.fromKaver.PostKaver;
import com.example.server.exceptions.PostNotFoundException;
import com.example.server.repository.posts.PostKaverRepository;
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
public class PostKaverService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final PostKaverRepository postRepository;

    @Autowired
    public PostKaverService(PostKaverRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostKaver> getAllPosts() {

        return postRepository.findAll();
    }

    public List<PostKaver> getPostsByGenre(String genre) {
        return postRepository.findPostKaverByGenre(genre);
    }

    public PostKaver getPostById(Long postId) {
        PostKaver post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post cannot be found"));
        LOG.info(post.getTitle());
        return post;
    }

    public PostKaver updatePost(PostKaver post, PostKaverDTO postDTO) {
        post.setTitle(postDTO.getTitle());
        post.setInfo(postDTO.getInfo());
        post.setGenre(postDTO.getGenre());
        post.setAddress(postDTO.getAddress());
        post.setImage(postDTO.getImage());
        post.setMainImage(postDTO.getMainImage());

        return postRepository.save(post);
    }

    public PostKaver likePost(Long postId, String username) {
        PostKaver post = postRepository.findById(postId)
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
        PostKaver post = getPostById(postId);
        postRepository.delete(post);
    }

    public void createPost() throws IOException {
        for (int i = 0; i < 5; i++) {
            String ref = null;
            if (i == 0) {
                ref = "https://kaverafisha.ru/moscow/places?type=restaurant";
            }
            if (i == 1) {
                ref = "https://kaverafisha.ru/moscow/places?type=club";
            }
            if (i == 2) {
                ref = "https://kaverafisha.ru/moscow/places?type=park";
            }
            if (i == 3) {
                ref = "https://kaverafisha.ru/moscow/places?type=artplace";
            }
            if (i == 4) {
                ref = "https://kaverafisha.ru/moscow/places?type=bar";
            }

            Document doc = Jsoup
                    //.connect("https://afisha.yandex.ru/moscow/selections/all-events-standup")
                    .connect(ref)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                    .get();

            Elements postTitleEl = doc.
                    getElementsByClass("bg-white rounded shadow-sm");

            for (Element el : postTitleEl) {
                String detailsLink = "https://kaverafisha.ru" + el
                        .getElementsByClass("bg-white rounded shadow-sm")
                        .attr("href");
                PostKaver post = new PostKaver();

                post.setTitle(el
                        .getElementsByClass("font-bold text-xl leading-tight mb-3")
                        .text());

                post.setMainImage(el
                        .getElementsByClass("w-full h-full object-cover rounded-md")
                        .attr("src"));

                post.setAddress(el
                        .getElementsByClass("text-gray-500 text-sm leading-tight")
                        .text());

                post.setLikes(0);
                post.setReferenceInfo("/kaver-info/info/");

                post.setDetailsLink(detailsLink);

                postRepository.save(post);
            }
        }
    }

    public void createPostDetails() throws IOException {
        List<PostKaver> allPosts = postRepository.findAll();
        for (PostKaver post : allPosts) {
            if (post.getGenre() != null) continue;
            if (post.getDetailsLink() == null) continue;
            Document postDetails = Jsoup.connect(post.getDetailsLink()).get();
            post.setGenre(postDetails
                    .getElementsByClass("border-b-2 pb-0.5 border-kaverRed")
                    .text());

            post.setInfo(postDetails
                    .getElementsByClass("md:text-lg max-w-3xl whitespace-pre-line mb-10")
                    .text());

            StringBuilder image = new StringBuilder(postDetails
                    .getElementsByClass("promo-media-background promo-media-background_type_image i-metrika-timing content-event-emotional__media i-bem")
                    .attr("style"));

            postRepository.save(post);
        }
    }
}
