package com.url.shortener.controller;

import com.url.shortener.service.HashService;
import com.url.shortener.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.result.view.RedirectView;
import reactor.core.publisher.Mono;

import static com.url.shortener.validator.Validator.validateNullOrEmpty;

@Controller
public class RedirectUrlController {

    private final UrlService urlService;
    private final HashService hashService;

    public RedirectUrlController(UrlService urlService, HashService hashService) {
        this.urlService = urlService;
        this.hashService = hashService;
    }

    @GetMapping("/{id}")
    public Mono<RedirectView> shortUrlToUrlRedirect(@PathVariable String id) {
        validateNullOrEmpty(id);
        String shortUrl = hashService.toShortUrl(id);

        /*
         * We could use 301(MOVED_PERMANENTLY) or 302(FOUND)
         * It depends on a question - do we need analytics or not?
         */
        return urlService.shortUrlToUrl(shortUrl)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("You have a wrong url, try another please")))
                .map(url -> new RedirectView(url.getLongUrl(), HttpStatus.FOUND));
    }

}
