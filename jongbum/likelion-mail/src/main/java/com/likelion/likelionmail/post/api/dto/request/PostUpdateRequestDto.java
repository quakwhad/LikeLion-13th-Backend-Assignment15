package com.likelion.likelionmail.post.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PostUpdateRequestDto(
        @NotBlank(message = "제목은 필수로 입력해야 합니다.")
        String title,
        @NotBlank(message = "내용은 필수로 입력해야 합니다.")
        String content
) {
}
