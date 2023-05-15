package com.example.server.services.posts;

import com.example.server.dto.PostFilmDTO;
import com.example.server.entity.film.PostFilm;
import com.example.server.exceptions.PostNotFoundException;
import com.example.server.repository.posts.PostFilmRepository;
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
public class PostFilmService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final PostFilmRepository postRepository;

    @Autowired
    public PostFilmService(PostFilmRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostFilm> getAllPosts() {
        return postRepository.findAll();
    }

    public PostFilm getPostById(Long postId) {
        PostFilm post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post cannot be found"));
        LOG.info(post.getTitle());
        return post;
    }

    public PostFilm updatePost(PostFilm post, PostFilmDTO postDTO) {
        post.setTitle(postDTO.getTitle());
        post.setInfo(postDTO.getInfo());
        post.setShortInfo(postDTO.getShortInfo());
        post.setGenre(postDTO.getGenre());
        post.setCinema(postDTO.getCinema());
        post.setImage(postDTO.getImage());
        post.setBackgroundImg(postDTO.getBackgroundImg());

        return postRepository.save(post);
    }

    public PostFilm likePost(Long postId, String username) {
        PostFilm post = postRepository.findById(postId)
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
        PostFilm post = getPostById(postId);
        postRepository.delete(post);
    }

    public void createPost() throws IOException {
        Document doc = Jsoup
                .connect("https://afisha.yandex.ru/moscow/selections/cinema-today")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                .get();

        Elements postTitleEl = doc.
                getElementsByClass("event events-list__item yandex-sans");

        for (Element el : postTitleEl) {
            String detailsLink = "https://afisha.yandex.ru" + el
                    .getElementsByAttributeValue("data-testid", "event-card-link")
                    .attr("href");
            PostFilm post = new PostFilm();

            post.setTitle(el
                    .getElementsByAttributeValue("data-component", "EventCard__EventInfo__Title")
                    .first()
                    .text());

            post.setCinema(el
                    .getElementsByAttributeValue("data-component", "EventCard__EventInfo__Details")
                    .first()
                    .text());

            post.setBackgroundImg(el
                    .getElementsByClass("NqGVWi")
                    .attr("src"));

            post.setLikes(0);

            post.setDetailsLink(detailsLink);

            postRepository.save(post);
        }
    }

    public void createPostDetails() throws IOException {
        List<PostFilm> allPosts = postRepository.findAll();
        for (PostFilm post : allPosts) {
            if (post.getGenre() != null) continue;
            if (post.getDetailsLink() == null) continue;
            Document postDetails = Jsoup.connect(post.getDetailsLink()).get();
            post.setGenre(postDetails
                    .getElementsByClass("tags tags_size_l tags_theme_light event-concert-heading__tags")
                    .text());

            post.setShortInfo(postDetails
                    .getElementsByClass("event-concert-description__argument yandex-sans")
                    .text());

            post.setInfo(postDetails
                    .getElementsByClass("concert-description__text-wrap")
                    .text() + "\n" + postDetails
                    .getElementsByClass("event-attributes__inner")
                    .text());

            post.setRating(postDetails.
                    getElementsByClass("Value-ie9gjh-2 iZlkBd")
                    .text());

            post.setImage(postDetails
                    .getElementsByClass("image event-concert-heading__poster")
                    .attr("src"));

            postRepository.save(post);
        }
    }
}
