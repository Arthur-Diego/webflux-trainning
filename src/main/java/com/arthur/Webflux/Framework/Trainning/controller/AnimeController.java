package com.arthur.Webflux.Framework.Trainning.controller;

import com.arthur.Webflux.Framework.Trainning.model.Anime;
import com.arthur.Webflux.Framework.Trainning.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/animes")
@Slf4j
public class AnimeController {

    private final AnimeService service;

    @GetMapping
    public Flux<Anime> listAll(){
        return service.findAll();
    }

    @GetMapping("/findbyid/{id}")
    public Mono<Anime> findById(@PathVariable("id") Integer id){
        return service.findById(id);
    }

}
