package com.url.shortener.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Url {

    @JsonIgnore
    private Long id;

    private String longUrl;

    private String shortUrl;

    public Url() {
    }

    public Url(Long id, String longUrl, String shortUrl) {
        this.id = id;
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }

    public Long getId() {
        return id;
    }

    public Url setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public Url setLongUrl(String longUrl) {
        this.longUrl = longUrl;
        return this;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public Url setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
        return this;
    }
}
