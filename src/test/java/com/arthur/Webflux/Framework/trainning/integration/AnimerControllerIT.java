package com.arthur.webflux.framework.trainning.integration;

import com.arthur.webflux.framework.trainning.controller.AnimeController;
import com.arthur.webflux.framework.trainning.model.Anime;
import com.arthur.webflux.framework.trainning.repository.AnimeRepository;
import com.arthur.webflux.framework.trainning.service.AnimeService;
import com.arthur.webflux.framework.trainning.util.AnimeCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;


import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = AnimeController.class)
@Import(AnimeService.class)
public class AnimerControllerIT {

    @MockBean
    private AnimeRepository animeRepositoryMock;

    @Autowired
    private WebTestClient webTestClient;//client reativo

    private final Anime anime = AnimeCreator.createValidAnime();

    @BeforeEach
    public void setup() {
        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(Flux.just(anime));

        BDDMockito.when(animeRepositoryMock.findById(anyInt()))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeRepositoryMock.save(any(Anime.class)))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeRepositoryMock.delete(any(Anime.class)))
                .thenReturn(Mono.empty());

        BDDMockito.when(animeRepositoryMock.save(anime))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("listAll returns a flux of anime")
    public void findAll_ReturnFluxAnime_WhenSuccessful() {
        webTestClient
                .get()
                .uri("/animes") // Contexto
                .exchange() // ResponseSpec
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(anime.getId())//Capturar a posicao do Json
                .jsonPath("$.[0].nome").isEqualTo(anime.getNome()); //Capturar a posicao do Json

    }

    @Test
    @DisplayName("listAll returns a flux of anime")
    public void listAll_Flavor2_ReturnFluxOfAnime_WhenSuccessful() {
        webTestClient
                .get()
                .uri("/animes")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Anime.class)
                .consumeWith(response -> {
                    List<Anime> animes = response.getResponseBody();
                    assertThat(animes).isNotNull();
                    assertThat(animes).hasSize(1);

                    Anime actualAnime = animes.get(0);
                    assertThat(actualAnime.getId()).isEqualTo(anime.getId());
                    assertThat(actualAnime.getNome()).isEqualTo(anime.getNome());
                });
    }

    @Test
    @DisplayName("findById returns a Mono with anime when it exists")
    public void findById_ReturnMonoAnime_WhenSuccessful() {

        ;
        webTestClient.get()
                .uri("/animes/{id}", 1) // Contexto
                .exchange() // ResponseSpec
                .expectStatus().isOk()
                .expectBody(Anime.class)
                .consumeWith(response -> {
                    var animeResponse = response.getResponseBody();
                    assertThat(animeResponse).isNotNull();

                    Anime actualAnime = animeResponse;
                    assertThat(actualAnime.getId()).isEqualTo(anime.getId());
                    assertThat(actualAnime.getNome()).isEqualTo(anime.getNome());
                });
    }

    @Test
    @DisplayName("findById returns Mono error when anime does not exist")
    public void findById_ReturnMonoError_WhenEmptyMonoIsReturned() {

        BDDMockito.when(animeRepositoryMock.findById(anyInt()))
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/animes/{id}", 1) // Contexto
                .exchange().expectStatus().isNotFound()
                .expectBody()
                .consumeWith(response -> {
                    var animeResponse = response.getResponseBody();
                    assertThat(animeResponse).isNotNull();
                    assertThat(new String(animeResponse)).contains("\"status\":404");
                });
    }

    @Test
    @DisplayName("save creates an anime when successful")
    public void save_CreatesAnime_WhenSuccessful() {

        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();

        webTestClient.post()
                .uri("/animes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(animeToBeSaved)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Anime.class)
                .consumeWith(response -> {
                    Anime animeResponse = response.getResponseBody();
                    assertThat(animeResponse).isNotNull();
                    assertThat(animeResponse.getId()).isNotNull(); // Verificar se o ID foi gerado
                    assertThat(animeResponse.getNome()).isEqualTo(animeToBeSaved.getNome());
                });
    }

    @Test
    @DisplayName("save returns mono error with bad request when name is empty")
    public void save_ReturnsError_WhenNameIsEmpty() {

        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved().withNome(null);

        webTestClient.post()
                .uri("/animes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(animeToBeSaved)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(new String(response.getResponseBodyContent())).contains("\"status\":400");
                });
    }

    @Test
    @DisplayName("delete removes the anime when successful")
    public void delete_RemovesAnime_WhenSuccessful() {
        webTestClient
                .delete()
                .uri("/animes/{id}", 1) // Contexto
                .exchange() // ResponseSpec
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("delete returns Mono error when nome does not exist")
    public void delete_ReturnMonoError_WhenEmptyMonoIsReturned() {

        BDDMockito.when(animeRepositoryMock.findById(anyInt()))
                .thenReturn(Mono.empty());

        webTestClient
                .delete()
                .uri("/animes/{id}", 1) // Contexto
                .exchange() // ResponseSpec
                .expectStatus().isNotFound()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(new String(response.getResponseBodyContent())).contains("\"status\":404");
                });
        ;
    }

    @Test
    @DisplayName("update save updated anime and returns empty mono when successful")
    public void update_SaveUpdatedAnime_WhenSuccessful() {
        webTestClient
                .put()
                .uri("/animes/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(anime)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("update returns Mono error when anime does not exist")
    public void update_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/animes/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(anime)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(new String(response.getResponseBodyContent())).contains("\"status\":404");
                });
    }
}
