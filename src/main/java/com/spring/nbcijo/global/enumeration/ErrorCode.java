package com.spring.nbcijo.global.enumeration;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(BAD_REQUEST, "회원이 존재하지 않습니다."),
    NOT_FOUND_POST(BAD_REQUEST, "해당 글이 존재하지 않습니다."),

    INVALID_PASSWORD(BAD_REQUEST, "비밀번호가 일치하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
