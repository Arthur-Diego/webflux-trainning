package com.arthur.webflux.framework.trainning.repository;

import com.arthur.webflux.framework.trainning.model.Anime;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AnimeRepository extends ReactiveCrudRepository<Anime, Integer> {

    @Override
    Mono<Anime> findById(Integer id);

}
