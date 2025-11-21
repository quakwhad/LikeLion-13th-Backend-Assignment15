package com.likelion.likelionmail.common.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    /**
     * 404 NOT FOUND
     */
    MEMBER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 사용자가 없습니다. memberId = ", "NOT_FOUND_404"),
    POST_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다. postId = ", "NOT_FOUND_404"),

    /**
     * 500 INTERNAL SERVER ERROR
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러가 발생했습니다", "INTERNAL_SERVER_ERROR_500"),

    /**
     * 400 BAD REQUEST
     */
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "유효성 검사에 실패하였습니다.", "BAD_REQUEST_400"),
    ALREADY_EXISTS_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.", "BAD_REQUEST_400"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.", "BAD_REQUEST_400"),
    NO_AUTHORIZATION_EXCEPTION(HttpStatus.BAD_REQUEST, "권한이 없습니다.", "BAD_REQUEST_400");

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}