package com.url.shortener.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public final class Validator {

    private static final Logger log = LoggerFactory.getLogger(Validator.class);

    public static void validateNullOrEmpty(String str) {
        if (str == null || str.isBlank()) {
            log.warn("str cannot be empty or blank: {}", str);
            throw new IllegalArgumentException("URL cannot be empty or blank");
        }
    }

    public static void validateLength(int length, int x) {
        if (length > x) {
            log.warn("Length cannot be more than {}: {}", x, length);
            throw new IllegalArgumentException("This URL too long");
        }
    }


    public static void validateUrl(String url) {
        if (!isValidUrl(url)) {
            log.warn("This Url is invalid: {}", url);
            throw new IllegalArgumentException("This URL is invalid");
        }
    }


    private static boolean isValidUrl(String longUrl) {
        try {
            new URL(longUrl);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
