package com.example.server.services.posts;

import com.example.server.dto.posts.PostTheaterDTO;
import com.example.server.entity.theater.PostTheater;
import com.example.server.exceptions.PostNotFoundException;
import com.example.server.repository.posts.PostTheaterRepository;
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
public class PostTheaterService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final PostTheaterRepository postRepository;

    @Autowired
    public PostTheaterService(PostTheaterRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostTheater> getAllPosts() {
        return postRepository.findAll();
    }

    public PostTheater getPostById(Long postId) {
        PostTheater post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post cannot be found"));
        LOG.info(post.getTitle());
        return post;
    }

    public PostTheater updatePost(PostTheater post, PostTheaterDTO postDTO) {
        post.setTitle(postDTO.getTitle());
        post.setInfo(postDTO.getInfo());
        post.setShortInfo(postDTO.getShortInfo());
        post.setGenre(postDTO.getGenre());
        post.setImage(postDTO.getImage());
        post.setAddress(postDTO.getAddress());
        post.setRating(postDTO.getRating());

        return postRepository.save(post);
    }

    public PostTheater likePost(Long postId, String username) {
        PostTheater post = postRepository.findById(postId)
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
        return postRepository.save(post);
    }

    public void deletePost(Long postId) {
        PostTheater post = getPostById(postId);
        postRepository.delete(post);
    }

    public void createPost() throws IOException {
        Document doc = Jsoup
                .connect("https://afisha.yandex.ru/moscow/selections/standup-top")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                .get();

        Elements postTitleEl = doc.
                getElementsByClass("event events-list__item yandex-sans");

        for (Element el : postTitleEl) {
            String detailsLink = "https://afisha.yandex.ru" + el
                    .getElementsByAttributeValue("data-testid", "event-card-link")
                    .attr("href");
            PostTheater post = new PostTheater();

            post.setTitle(el
                    .getElementsByAttributeValue("data-component", "EventCard__EventInfo__Title")
                    .first()
                    .text());

            post.setShortInfo(el
                    .getElementsByAttributeValue("data-component", "EventCard__EventInfo__Details")
                    .first()
                    .text());

            post.setImage(el
                    .getElementsByClass("NqGVWi")
                    .attr("src"));

            post.setLikes(0);

            post.setDetailsLink(detailsLink);

            postRepository.save(post);
        }
    }

    public void createPostDetails() throws IOException {
        List<PostTheater> allPosts = postRepository.findAll();
        for (PostTheater post : allPosts) {
            if (post.getGenre() != null) continue;
            if (post.getDetailsLink() == null) continue;
            Document postDetails = Jsoup.connect(post.getDetailsLink()).get();
            post.setGenre(postDetails
                    .getElementsByClass("tags tags_size_l tags_theme_light event-concert-heading__tags")
                    .text());

            post.setShortInfo(post.getShortInfo()
                    + '\n' + postDetails
                    .getElementsByClass("event-concert-description__argument yandex-sans")
                    .text());

            post.setInfo(postDetails
                    .getElementsByClass("concert-description__text-wrap")
                    .text());

            post.setRating(postDetails.
                    getElementsByClass("Value-ie9gjh-2 iZlkBd")
                    .text());

            post.setAddress(postDetails
                    .getElementsByClass("place__tag")
                    .text() + "\n" + postDetails
                    .getElementsByClass("link link_theme_black i-metrika-block__click i-bem")
                    .text() + "\n" + postDetails
                    .getElementsByClass("place__address")
                    .text() + "\n" + postDetails
                    .getElementsByClass("metro place__metro place__metro_line_one")
                    .text());

            postRepository.save(post);
        }
    }
}