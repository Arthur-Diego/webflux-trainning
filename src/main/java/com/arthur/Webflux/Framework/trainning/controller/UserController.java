package com.arthur.webflux.framework.trainning.controller;

import com.arthur.webflux.framework.trainning.User;
import com.arthur.webflux.framework.trainning.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    Flux<User> list(){

        WebClient.create().get().uri("https://jsonplaceholder.typicode.com/todos")
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(s -> System.out.println("sd"))
                .subscribe(
                        item -> System.out.println(item),
                        error -> System.err.println("Errou"),
                        () -> System.out.println("Terminou!"));

        System.out.println("OPA AI KKKKK");


        return userRepository.findAll();
    }
}
