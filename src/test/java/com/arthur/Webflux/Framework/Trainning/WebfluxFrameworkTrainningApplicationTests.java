package com.arthur.Webflux.Framework.Trainning;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebfluxFrameworkTrainningApplicationTests {

    @Autowired
    private WebTestClient webClient;

    @Test
    void test() {

        User user = new User(null, "user", "1234", "arthur@teste.com.br");
        webClient.post().uri("/users").bodyValue(user)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(User.class)
                .value(userResponse -> {
                    assertNotNull(userResponse.id());
                    assertEquals(user.username(), userResponse.username());
                    assertEquals(user.password(), userResponse.password());
                    assertEquals(user.email(), userResponse.email());
                });
        webClient.get().uri("/users")
                .exchange()
                .expectStatus().is2xxSuccessful().expectBodyList(User.class)
                .value(response -> {
                    User getResponse = response.get(0);

                    assertNotNull(getResponse.id());
                    assertEquals(user.username(), getResponse.username());
                    assertEquals(user.password(), getResponse.password());
                    assertEquals(user.email(), getResponse.email());
                });
    }

}
