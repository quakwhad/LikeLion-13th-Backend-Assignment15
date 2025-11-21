package com.likelion.likelionmail.oauth.api.dto;

import lombok.Data;

@Data
public class KakaoUserInfo {
    private Long id;
    private KakaoAccount kakaoAccount;

    @Data
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Data
        public static class Profile {
            private String nickname;
            private String profileImageUrl;
        }
    }
}