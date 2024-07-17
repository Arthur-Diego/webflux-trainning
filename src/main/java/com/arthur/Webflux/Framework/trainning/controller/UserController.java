package com.arthur.webflux.framework.trainning.controller;

import com.arthur.webflux.framework.trainning.User;
import com.arthur.webflux.framework.trainning.UserRepository;
import com.arthur.webflux.framework.trainning.model.Anime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping
    Mono<User> Create(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/all")
    Flux<User> createUsers(@RequestBody List<User> users) {
        return userRepository.saveAll(users);
    }

    @GetMapping
    public Flux<TodoItem> list() {
        return WebClient.create()
                .get()
                .uri("https://jsonplaceholder.typicode.com/todos")
                .retrieve()
                .bodyToFlux(TodoItem.class);
    }

    static class TodoItem {
        private int userId;
        private int id;
        private String title;
        private boolean completed;

        // Getters and Setters
        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }
}


