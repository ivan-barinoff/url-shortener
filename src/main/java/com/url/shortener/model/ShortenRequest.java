package com.url.shortener.model;

public class ShortenRequest {
    private String url;

    public String getUrl() {
        return url;
    }

    public ShortenRequest setUrl(String url) {
        this.url = url;
        return this;
    }
}
