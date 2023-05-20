package com.url.shortener.repository;

import com.url.shortener.model.Url;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UrlRepository extends ReactiveCrudRepository<Url, Long> {
    Mono<Url> findByShortUrl(String shortUrl);
}
