package com.url.shortener.service;

import com.url.shortener.model.Url;
import com.url.shortener.repository.UrlRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final HashService hashService;

    public UrlService(UrlRepository urlRepository,
                      HashService hashService
    ) {
        this.urlRepository = urlRepository;
        this.hashService = hashService;
    }

    public Mono<Url> shortUrlToUrl(String shortUrl) {
        //todo cache
        return urlRepository.findByShortUrl(shortUrl);
    }

    public Mono<Url> urlToShortUrl(String url) {
        /*
         * It is better to think about uniqueId generator using seed, machineID or another ID
         * It will be a guarantee that the number is a unique number
         * Hash functions(SHA-256, MD or UUID) could have collisions and need to fix them
         * using loop + an additional request to our database (it is a performance issue)
         */
        long uniqueId = System.currentTimeMillis();
        String shortUrl = hashService.stringToHashBase62(uniqueId);

        return urlRepository.save(new Url(uniqueId, url, shortUrl));
    }

}
