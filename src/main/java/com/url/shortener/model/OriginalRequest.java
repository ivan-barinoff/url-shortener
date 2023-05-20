package com.url.shortener.model;

public class OriginalRequest {
    private String shortUrl;

    public String getShortUrl() {
        return shortUrl;
    }

    public OriginalRequest setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
        return this;
    }
}
