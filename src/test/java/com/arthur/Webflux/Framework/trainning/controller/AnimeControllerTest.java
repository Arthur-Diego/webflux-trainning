package com.arthur.webflux.framework.trainning.controller;

import com.arthur.webflux.framework.trainning.model.Anime;
import com.arthur.webflux.framework.trainning.service.AnimeService;
import com.arthur.webflux.framework.trainning.util.AnimeCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeService;

    private final Anime anime = AnimeCreator.createValidAnime();
    private final Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();

    @BeforeAll
    public static void blockHoundSetup(){
        BlockHound.install();
    }

    @Test
    public void blockHoundWorks(){
        try {
            FutureTask<?> task = new FutureTask<>( () -> {
                Thread.sleep(0);
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("should fail");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

    @BeforeEach
    public void setup(){
        BDDMockito.when(animeService.findAll())
                .thenReturn(Flux.just(anime));

        BDDMockito.when(animeService.findById(anyInt()))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeService.save(animeToBeSaved))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeService.delete(anyInt()))
                .thenReturn(Mono.empty());


    }

    @Test
    @DisplayName("findAll returns a flux of anime")
    public void findAll_ReturnFluxAnime_WhenSuccessful(){
        StepVerifier.create(animeController.listAll())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById retirns a Mono with anime when it exists")
    public void findById_ReturnMonoError_WhenEmptyMonoIsReturned(){

        StepVerifier.create(animeController.findById(1))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("save creates an anime when successful")
    public void save_CreatesAnime_WhenSuccessful(){

        StepVerifier.create(animeController.save(animeToBeSaved))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete removes the anime when successful")
    public void delete_DeleteAnime_WhenSuccessful(){

        StepVerifier.create(animeController.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update save updated anime and returns empty mono when successful")
    public void update_SaveUpdatedAnime_WhenSuccessful(){
        Anime animeToUpdate = AnimeCreator.createValidAnime();
        BDDMockito.when(animeService.update(animeToUpdate))
                .thenReturn(Mono.empty());
        StepVerifier.create(animeController.update(1, animeToUpdate))
                .expectSubscription()
                .verifyComplete();
    }


}