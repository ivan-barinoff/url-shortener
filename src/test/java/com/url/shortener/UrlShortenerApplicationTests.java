package com.url.shortener;

import com.url.shortener.controller.UrlShortenerController;
import com.url.shortener.model.OriginalRequest;
import com.url.shortener.model.ShortenRequest;
import com.url.shortener.model.Url;
import com.url.shortener.service.UrlService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = UrlShortenerController.class)
class UrlShortenerApplicationTests {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private UrlService urlService;

    @Test
    void testShortenEmpty() {
        ShortenRequest request = new ShortenRequest().setUrl("");

        webClient.post()
                .uri("/api/v1/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isBadRequest();

        verify(urlService, times(0)).shortUrlToUrl(any());
    }

    @Test
    void testShortenTooLong() {
        ShortenRequest request = new ShortenRequest().setUrl("https://github.com/" + "long".repeat(70));

        webClient.post()
                .uri("/api/v1/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isBadRequest();

        verify(urlService, times(0)).shortUrlToUrl(any());
    }

    @Test
    void testShortenWrongUrl() {
        ShortenRequest request = new ShortenRequest().setUrl("github");

        webClient.post()
                .uri("/api/v1/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isBadRequest();

        verify(urlService, times(0)).shortUrlToUrl(any());
    }

    @Test
    void testOriginalWrongUrl() {
        OriginalRequest request = new OriginalRequest().setShortUrl("github");

        webClient.post()
                .uri("/api/v1/original")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isBadRequest();

        verify(urlService, times(0)).shortUrlToUrl(any());
    }

    @Test
    void testOriginalTooLong() {
        OriginalRequest request = new OriginalRequest().setShortUrl("https://github.com/" + "long".repeat(13));

        webClient.post()
                .uri("/api/v1/original")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isBadRequest();

        verify(urlService, times(0)).shortUrlToUrl(any());
    }

    @Test
    void testOriginalNotFound() {
        String shortUrl = "http://localhost:9000/abcdf22";

        when(urlService.shortUrlToUrl(shortUrl)).thenReturn(Mono.empty());

        OriginalRequest request = new OriginalRequest().setShortUrl(shortUrl);

        webClient.post()
                .uri("/api/v1/original")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isBadRequest();

        verify(urlService)
                .shortUrlToUrl(
                        ArgumentMatchers.eq(shortUrl)
                );
        verify(urlService, times(1)).shortUrlToUrl(any());
    }

    @Test
    void testOriginalFound() {
        String shortUrl = "http://localhost:9000/abcdf22";
        String longUrl = "https://github.com/";

        when(urlService.shortUrlToUrl(shortUrl)).thenReturn(Mono.just(new Url().setLongUrl(longUrl).setShortUrl(shortUrl)));

        OriginalRequest request = new OriginalRequest().setShortUrl(shortUrl);

        webClient.post()
                .uri("/api/v1/original")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.shortUrl").isEqualTo(shortUrl)
                .jsonPath("$.longUrl").isEqualTo(longUrl);

        verify(urlService)
                .shortUrlToUrl(
                        ArgumentMatchers.eq(shortUrl)
                );
        verify(urlService, times(1)).shortUrlToUrl(any());
    }

    @Test
    void testOriginalRedirect() {
        String shortUrl = "http://localhost:9000/abcdf22";
        String longUrl = "https://github.com/";

        when(urlService.shortUrlToUrl(shortUrl)).thenReturn(Mono.just(new Url().setLongUrl(longUrl).setShortUrl(shortUrl)));

        OriginalRequest request = new OriginalRequest().setShortUrl(shortUrl);

        webClient.post()
                .uri("/api/v1/original")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.shortUrl").isEqualTo(shortUrl)
                .jsonPath("$.longUrl").isEqualTo(longUrl);

        verify(urlService)
                .shortUrlToUrl(
                        ArgumentMatchers.eq(shortUrl)
                );
        verify(urlService, times(1)).shortUrlToUrl(any());
    }

}
