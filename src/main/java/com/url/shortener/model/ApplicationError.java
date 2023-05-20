package com.url.shortener.model;

public class ApplicationError {

    private String message;
    private Integer code;

    public String getMessage() {
        return message;
    }

    public ApplicationError setMessage(String message) {
        this.message = message;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public ApplicationError setCode(Integer code) {
        this.code = code;
        return this;
    }
}
