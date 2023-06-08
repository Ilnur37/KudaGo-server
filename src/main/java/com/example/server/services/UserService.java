package com.example.server.services;

import com.example.server.dto.UserDTO;
import com.example.server.entity.User;
import com.example.server.entity.concert.PostConcert;
import com.example.server.entity.enums.ERole;
import com.example.server.entity.film.PostFilm;
import com.example.server.entity.standUp.PostStandUp;
import com.example.server.exceptions.UserExistException;
import com.example.server.facade.posts.PostConcertFacade;
import com.example.server.facade.posts.PostFilmFacade;
import com.example.server.facade.posts.PostStandUpFacade;
import com.example.server.payload.request.SignupRequest;
import com.example.server.repository.UserRepository;
import com.example.server.repository.posts.PostConcertRepository;
import com.example.server.repository.posts.PostFilmRepository;
import com.example.server.repository.posts.PostStandUpRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.security.Principal;
import java.util.*;
import java.util.List;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private PostFilmRepository postFilmRepository;
    private PostFilmFacade postFilmFacade;
    private PostStandUpRepository postStandUpRepository;
    private PostStandUpFacade postStandUpFacade;
    private PostConcertRepository postConcertRepository;
    private PostConcertFacade postConcertFacade;
    private List<Integer> indexOfPostToDelete = new ArrayList<>();

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                       PostFilmRepository postFilmRepository, PostFilmFacade postFilmFacade,
                       PostStandUpRepository postStandUpRepository, PostStandUpFacade postStandUpFacade,
                       PostConcertRepository postConcertRepository, PostConcertFacade postConcertFacade) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.postFilmRepository = postFilmRepository;
        this.postFilmFacade = postFilmFacade;
        this.postStandUpRepository = postStandUpRepository;
        this.postStandUpFacade = postStandUpFacade;
        this.postConcertRepository = postConcertRepository;
        this.postConcertFacade = postConcertFacade;
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    public User getUserById(long id) {
        return userRepository.findUserById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
    }

    public User createUser(SignupRequest userIn) {
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.setRole(ERole.ROLE_USER);

        try {
            LOG.info("Saving User {}", userIn.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            LOG.info("Error during registration, {}", e.getMessage());
            throw new UserExistException("The user " + user.getUsername() + "already exist");
        }
    }

    public List<Object> getFavouritePosts(Principal principal) {
        List<Object> favouritePosts = new ArrayList<>();
        User user = getUserByPrincipal(principal);
        String name = user.getUsername();

        List<PostFilm> postFilms = postFilmRepository.findAll();
        for (PostFilm post : postFilms) {
            if (post.getLikedUser().contains(name)) {
                favouritePosts.add(postFilmFacade.postToPostFilmDTO(post));
            }
        }

        List<PostStandUp> postStandUps = postStandUpRepository.findAll();
        for (PostStandUp post : postStandUps) {
            if (post.getLikedUser().contains(name)) {
                favouritePosts.add(postStandUpFacade.postToPostStandUpDTO(post));
            }
        }

        List<PostConcert> postConcerts = postConcertRepository.findAll();
        for (PostConcert post : postConcerts) {
            if (post.getLikedUser().contains(name)) {
                favouritePosts.add(postConcertFacade.postToPostConcertDTO(post));
            }
        }

        return favouritePosts;
    }

    public List<Object> getRecommendations(Principal principal) {
        this.indexOfPostToDelete.clear();
        User user = getUserByPrincipal(principal);
        String name = user.getUsername();

        List<Object> recommendations = new ArrayList<>();
        HashMap<String, Integer> countGenre = new HashMap<>();
        List<String> topGenres;
        int count = 0;

        List<PostFilm> postsFilm = postFilmRepository.findAll();
        for (int i = 0; i < postsFilm.size() - 1; i++) {
            if (postsFilm.get(i).getLikedUser().contains(name)) {
                String[] genres = postsFilm.get(i).getGenre().split("-");
                this.indexOfPostToDelete.add(i);
                for (String str : genres) {
                    if (countGenre.containsKey(str)) {
                        countGenre.put(str, countGenre.get(str) + 1);
                    } else {
                        countGenre.put(str, 1);
                    }
                }
            }
        }
        for (int i = this.indexOfPostToDelete.size() - 1; i > -1; i --) {
            postsFilm.remove(Integer.parseInt(String.valueOf(this.indexOfPostToDelete.get(i))));
        }
        topGenres = this.sortByValue(countGenre);
        for (PostFilm post : postsFilm) {
            if (count > 2) break;
            for (String str : topGenres) {
                if (post.getGenre().contains(str)) {
                    recommendations.add(postFilmFacade.postToPostFilmDTO(post));
                    count++;
                    break;
                }
            }
        }
        countGenre.clear();
        topGenres.clear();
        this.indexOfPostToDelete.clear();
        count = 0;


        List<PostStandUp> postStandUps = postStandUpRepository.findAll();
        for (int i = 0; i < postStandUps.size() - 1; i++) {
            if (postStandUps.get(i).getLikedUser().contains(name)) {
                String[] genres = postStandUps.get(i).getGenre().split("-");
                this.indexOfPostToDelete.add(i);
                for (String str : genres) {
                    if (countGenre.containsKey(str)) {
                        countGenre.put(str, countGenre.get(str) + 1);
                    } else {
                        countGenre.put(str, 1);
                    }
                }
            }
        }
        for (int i = this.indexOfPostToDelete.size() - 1; i > -1; i --) {
            postStandUps.remove(Integer.parseInt(String.valueOf(this.indexOfPostToDelete.get(i))));
        }
        topGenres = this.sortByValue(countGenre);
        for (PostStandUp post : postStandUps) {
            if (count > 2) break;
            for (String str : topGenres) {
                if (post.getGenre().contains(str)) {
                    recommendations.add(postStandUpFacade.postToPostStandUpDTO(post));
                    count++;
                    break;
                }
            }
        }
        countGenre.clear();
        topGenres.clear();
        this.indexOfPostToDelete.clear();
        count = 0;


        List<PostConcert> postConcert = postConcertRepository.findAll();
        for (int i = 0; i < postConcert.size() - 1; i++) {
            if (postConcert.get(i).getLikedUser().contains(name)) {
                String[] genres = postConcert.get(i).getGenre().split("-");
                this.indexOfPostToDelete.add(i);
                for (String str : genres) {
                    if (countGenre.containsKey(str)) {
                        countGenre.put(str, countGenre.get(str) + 1);
                    } else {
                        countGenre.put(str, 1);
                    }
                }
            }
        }
        for (int i = this.indexOfPostToDelete.size() - 1; i > -1; i --) {
            postConcert.remove(Integer.parseInt(String.valueOf(this.indexOfPostToDelete.get(i))));
        }
        topGenres = this.sortByValue(countGenre);
        for (PostConcert post : postConcert) {
            if (count > 2) break;
            for (String str : topGenres) {
                if (post.getGenre().contains(str)) {
                    recommendations.add(postConcertFacade.postToPostConcertDTO(post));
                    count++;
                    break;
                }
            }
        }

        return recommendations;
    }

    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        user.setName(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setBio(userDTO.getBio());

        return userRepository.save(user);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username " + username));
    }

    private List<String> sortByValue(HashMap<String, Integer> hm) {
        List<String> topGenres = new ArrayList<>();
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        // Sort the list
        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        for (int i = list.size() - 1; i > -1; i--) {
            topGenres.add(list.get(i).getKey());
            if (topGenres.size() > 4) break;
        }

        return topGenres;
    }
}
