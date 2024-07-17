package com.arthur.webflux.framework.trainning.service;

import com.arthur.webflux.framework.trainning.model.Anime;
import com.arthur.webflux.framework.trainning.repository.AnimeRepository;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public Flux<Anime> findAll(){
        return animeRepository.findAll();
    }

    public Mono<Anime> findById(Integer id){
        return animeRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException())
                .log();
    }

    private <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
    }

    public Mono<Anime> save(Anime anime){
        return animeRepository.save(anime);
    }

    @Transactional
    public Flux<Anime> saveAll(List<Anime> animes){
        return animeRepository.saveAll(animes)
                .doOnNext(this::trhowResponseStatusExceptionWhenEmptyName);
    }

    private void trhowResponseStatusExceptionWhenEmptyName(Anime anime){
        if(StringUtil.isNullOrEmpty(anime.getNome())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name");
        }
    }

    public Mono<Void> update(Anime anime){
        return findById(anime.getId())
                .flatMap(animeRepository::save)
                .thenEmpty(Mono.empty());
    }


    public Mono<Void> delete(Integer id) {
        return findById(id)
                .flatMap(animeRepository::delete);
    }

    public void teste() {
        Mono.fromCallable(() -> {
                    // lógica aqui, será executada no boundedElastic Scheduler
                    System.out.println("Executando lógica dentro do Mono.fromCallable no boundedElastic Scheduler");
                    return "exemplo de dado";
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(data -> {
                    // esta operação será executada no boundedElastic Scheduler
                    System.out.println("Transformando dado para maiúsculas no map, no boundedElastic Scheduler");
                    return data.toUpperCase();
                })
                .subscribeOn(Schedulers.parallel())
                .map(s -> s.equals("kkk"))
                .publishOn(Schedulers.single())
                .doOnNext(data -> {
                    // esta operação será executada no single Scheduler
                    System.out.println("Processando dado no doOnNext, no single Scheduler: " + data);
                })
                .subscribe(result -> {
                    // lógica de processamento do resultado
                    System.out.println("Resultado recebido no subscribe: " + result);
                });
    }
}
