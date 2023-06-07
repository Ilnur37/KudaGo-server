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
import java.util.ArrayList;
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
}
