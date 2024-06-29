package com.arthur.Webflux.Framework.Trainning.repository;

import com.arthur.Webflux.Framework.Trainning.model.Anime;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AnimeRepository extends ReactiveCrudRepository<Anime, Integer> {

    Mono<Anime> findById(int id);

}
