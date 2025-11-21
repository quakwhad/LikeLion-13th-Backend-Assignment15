package com.likelion.likelionmail.oauth.api;

import com.likelion.likelionmail.oauth.api.dto.Token;
import com.likelion.likelionmail.oauth.application.AuthLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class AuthLoginController {

    private final AuthLoginService authLoginService;

    @GetMapping("/code/kakao")
    public Token kakaoCallback(@RequestParam(name = "code") String code) {
        // 사용자 정보 조회 + 회원 저장 + JWT 생성
        return authLoginService.kakaoLoginOrSignUp(code);
    }
}