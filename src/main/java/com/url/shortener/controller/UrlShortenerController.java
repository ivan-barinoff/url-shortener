package com.url.shortener.controller;

import com.url.shortener.model.OriginalRequest;
import com.url.shortener.model.ShortenRequest;
import com.url.shortener.model.Url;
import com.url.shortener.service.UrlService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.url.shortener.validator.Validator.validateNullOrEmpty;
import static com.url.shortener.validator.Validator.validateLength;
import static com.url.shortener.validator.Validator.validateUrl;

// todo Swagger or similar
@RestController
@RequestMapping(value = "/api/v1")
public class UrlShortenerController {

    private final UrlService urlService;

    public UrlShortenerController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public Mono<Url> urlToShortUrl(@RequestBody ShortenRequest request) {
        String longUrl = request.getUrl();
        validateNullOrEmpty(longUrl);

        int urlLength = longUrl.length();
        validateLength(urlLength, 255);

        validateUrl(longUrl);

        return urlService.urlToShortUrl(longUrl);
    }

    @PostMapping("/original")
    public Mono<Url> shortUrlToUrl(@RequestBody OriginalRequest request) {
        String shortUrl = request.getShortUrl();
        validateNullOrEmpty(shortUrl);

        int shortUrlLength = shortUrl.length();
        validateLength(shortUrlLength, 64);

        validateUrl(shortUrl);

        return urlService.shortUrlToUrl(shortUrl)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("You have a wrong url, try another please")));
    }

}
