package com.p8.common.net.exception;

import org.jetbrains.annotations.NotNull;

/**
 * @author : WX.Y
 * date : 2021/3/12 10:23
 * description :
 */
public class InterceptableException extends Exception {
    public String code;
    public String message;

    public InterceptableException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public InterceptableException(Throwable throwable, String code) {
        super(throwable);
        this.code = code;
    }

    @NotNull
    @Override
    public String toString() {
        return "InterceptableException{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public static final String TOKEN_TIMEOUT = "0004";
}