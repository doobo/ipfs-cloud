package com.github.doobo.params;

import lombok.Getter;

/**
 * 自定义异常
 */
public class CustomException extends RuntimeException {

    @Getter
    private int code;

    @Getter
    private String payload;

    public CustomException() {
    }

    public CustomException(String message) {
        this(0, message);
    }

    public CustomException(String message, String payload) {
        this(0, message, payload);
    }

    public CustomException(int code, String message, String payload) {
        super(message);
        this.payload = payload;
        this.code = code;
    }

    public CustomException(ErrorInfo errorInfo) {
        super(errorInfo.getMsg());
        this.payload = this.getMessage();
        this.code = errorInfo.getCode();
    }

    public CustomException(int code, String message) {
        super(message);
        this.payload = message;
        try {
            this.code = code;
        } catch (Exception ignore) {
        }
    }

}
