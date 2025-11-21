package com.likelion.likelionmail.mail.api.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MailRequest {
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String to;
}

