package com.url.shortener;

import com.url.shortener.model.OriginalRequest;
import com.url.shortener.model.ShortenRequest;
import com.url.shortener.model.Url;
import com.url.shortener.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlShortenerIntegrationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UrlRepository urlRepository;

    @BeforeEach
    public void setup() {
//        urlRepository.deleteAll().switchIfEmpty(Mono.empty()).block();
    }

    @Test
    public void testShortenURL() {
        String longURL = "https://github.com/";
        ShortenRequest request = new ShortenRequest().setUrl(longURL);

        AtomicReference<String> shortUrl = new AtomicReference<>();
        webTestClient.post()
                .uri("/api/v1/shorten")
                .body(Mono.just(request), ShortenRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Url.class)
                .value(url -> {
                    assertNotNull(url);
                    assertEquals(longURL, url.getLongUrl());
                    assertNotNull(url.getShortUrl());

                    shortUrl.set(url.getShortUrl());
                    urlRepository.findByShortUrl(url.getShortUrl())
                            .subscribe(foundUrl -> {
                                assertNotNull(foundUrl);
                                assertEquals(foundUrl.getLongUrl(), longURL);
                            });
                });

        OriginalRequest originalRequest = new OriginalRequest().setShortUrl(shortUrl.get());
        webTestClient.post()
                .uri("/api/v1/original")
                .body(Mono.just(originalRequest), OriginalRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Url.class)
                .value(url -> {
                    assertNotNull(url);
                    assertNotNull(url.getShortUrl());
                    assertNotNull(url.getLongUrl());
                });

        webTestClient.get()
                .uri("/{shortURL}", shortUrl.get().replaceAll("http://localhost:0/", ""))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", longURL);
    }
}