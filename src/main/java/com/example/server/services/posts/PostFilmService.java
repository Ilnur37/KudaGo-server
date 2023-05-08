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
import java.util.Objects;
import java.util.Optional;

@Service
public class PostFilmService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final PostFilmRepository postFilmRepository;

    @Autowired
    public PostFilmService(PostFilmRepository postFilmRepository) {
        this.postFilmRepository = postFilmRepository;
    }

    public PostFilm createPost(PostFilmDTO postDTO) {
        PostFilm post = new PostFilm();
        post.setTitle(postDTO.getTitle());
        post.setInfo(postDTO.getInfo());
        post.setShortInfo(postDTO.getShortInfo());
        post.setGenre(postDTO.getGenre());
        post.setCinema(postDTO.getCinema());
        post.setImage(postDTO.getImage());
        post.setBackgroundImg(post.getBackgroundImg());
        post.setLikes(0);

        return postFilmRepository.save(post);
    }

    public List<PostFilm> getAllPosts() {
        return postFilmRepository.findAll();
    }

    public PostFilm getPostById(Long postId) {
        PostFilm postFilm = postFilmRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post cannot be found"));
        LOG.info(postFilm.getTitle());
        return postFilm;
    }

    public PostFilm updatePost(PostFilm postFilm, PostFilmDTO postDTO) {
        postFilm.setTitle(postDTO.getTitle());
        postFilm.setInfo(postDTO.getInfo());
        postFilm.setShortInfo(postDTO.getShortInfo());
        postFilm.setGenre(postDTO.getGenre());
        postFilm.setCinema(postDTO.getCinema());
        postFilm.setImage(postDTO.getImage());
        postFilm.setBackgroundImg(postDTO.getBackgroundImg());

        return postFilmRepository.save(postFilm);
    }

    public PostFilm likePost(Long postId, String username) {
        PostFilm post = postFilmRepository.findById(postId)
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
        return postFilmRepository.save(post);
    }

    public void deletePost(Long postId) {
        PostFilm post = getPostById(postId);
        postFilmRepository.delete(post);
    }


    public void createFilm() throws IOException {
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
            PostFilm film = new PostFilm();

            film.setTitle(el
                    .getElementsByAttributeValue("data-component", "EventCard__EventInfo__Title")
                    .first()
                    .text());

            film.setCinema(el
                    .getElementsByAttributeValue("data-component", "EventCard__EventInfo__Details")
                    .first()
                    .text());

            film.setBackgroundImg(el
                    .getElementsByClass("NqGVWi")
                    .attr("src"));

            film.setDetailsLink(detailsLink);

            postFilmRepository.save(film);
        }
    }

    public void createFilmDetails() throws IOException {
        List<PostFilm> allFilms = postFilmRepository.findAll();
        for (PostFilm film : allFilms) {
            if (film.getGenre() != null) continue;
            if (film.getDetailsLink() == null) continue;
            Document postDetails = Jsoup.connect(film.getDetailsLink()).get();
            film.setGenre(postDetails
                    .getElementsByClass("tags tags_size_l tags_theme_light event-concert-heading__tags")
                    .text());

            film.setShortInfo(postDetails
                    .getElementsByClass("event-concert-description__argument yandex-sans")
                    .text());

            film.setInfo(postDetails
                    .getElementsByClass("concert-description__text-wrap")
                    .text() + "\n" + postDetails
                    .getElementsByClass("event-attributes__inner")
                    .text());

            film.setRating(postDetails.
                    getElementsByClass("Value-ie9gjh-2 iZlkBd")
                    .text());

            film.setImage(postDetails
                    .getElementsByClass("image event-concert-heading__poster")
                    .attr("src"));



            /*int j = 0;
            String result = "";
            while (!postDetails
                    .getElementsByClass("event-attributes__category").get(j).text().equals("Год производства")) {
                result += postDetails
                        .getElementsByClass("event-attributes__category").get(j).text()
                        + ' ' +
                        postDetails
                                .getElementsByClass("event-attributes__category-value").get(j).text()
                        + '\n';
                j++;
            }
            result += postDetails
                    .getElementsByClass("event-attributes__category").get(j).text()
                    + ' ' +
                    postDetails
                            .getElementsByClass("event-attributes__category-value").get(j).text()
                    + '\n';

            film.setInfo(result);*/

            postFilmRepository.save(film);
        }
    }
}
