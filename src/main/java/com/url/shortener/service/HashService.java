package com.url.shortener.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Service
public class HashService {

    private final String shortenerUrl;

    public final int maxLength;
    private static final char[] ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public HashService(@Value("${shortener.url}") String shortenerUrl,
                       @Value("${shortener.max-length}") int maxLength) {
        this.shortenerUrl = shortenerUrl;
        this.maxLength = maxLength;
    }

    public String stringToHashUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, maxLength);
    }

    public String stringToHashBase62Random() {
        int n = ALPHABET.length;

        StringBuilder result = new StringBuilder();
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < maxLength; i++) {
            result.append(ALPHABET[r.nextInt(n)]);
        }
        return result.toString();
    }

    public String stringToHashBase64(String url) {
        return Base64.getEncoder().encodeToString(url.getBytes());
    }

    public String stringToHashBase62(long uniqueId) {
        return toShortUrl(convertToBase62(uniqueId, maxLength));
    }

    public static String convertToBase62(long number, int length) {
        char[] result = new char[length];

        for (int i = length - 1; i >= 0 && number >= 0; i--) {
            result[i] = ALPHABET[(int) (number % 62)];
            number /= 62;
        }

        System.out.println(result);
        return String.valueOf(result);
    }

    public String toShortUrl(String id) {
        return String.format("%s%s", shortenerUrl, id);
    }
}
